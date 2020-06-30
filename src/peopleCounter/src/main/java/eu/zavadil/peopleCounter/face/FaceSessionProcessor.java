package eu.zavadil.peopleCounter.face;

import eu.zavadil.peopleCounter.core.Logger;
import eu.zavadil.peopleCounter.core.LoggerLevel;
import eu.zavadil.peopleCounter.persistence.dao.FaceDao;
import eu.zavadil.peopleCounter.persistence.dao.PictureDao;
import eu.zavadil.peopleCounter.persistence.model.Face;
import eu.zavadil.peopleCounter.persistence.model.Picture;
import eu.zavadil.peopleCounter.persistence.model.Session;
import eu.zavadil.peopleCounter.ui.MainWindowController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.io.File;
import java.util.List;

/**
 * Core of peopleCounter. 
 * This central object keeps track of running session and manages processing of all pictures and faces for the session.
 */
@Component
public class FaceSessionProcessor {
    
    @Autowired
    private Logger logger;

    @Autowired
    private PictureDao pictureDao;

    @Autowired
    private FaceDao faceDao;

    @Autowired
    MainWindowController mainWindowController;

    /* SESSION */
    
    /**
     * Current active session. All new pictures are automatically put here.
     * If there is no session a new pictures are sent for processing, new session is automatically created.
     */
    private Session current_session;

    public Session getCurrentSession() {
        return this.current_session;
    }

    /**
     * Close running session if there is any and return it.
     * @return Closed session.
     */
    public void closeSession() {
        this.stopProcessing();
        if (this.current_session != null) {
            logger.log("FaceSessionProcessor: Session %s closed.", this.current_session.getName());
            this.current_session = null;
        }
    }
    
    /**
     * Resume a session. If there is a session in progress, then it is closed before resuming.
     * @param session Session to be opened.
     */
    public void openSession(Session session) {
        this.closeSession();
        this.current_session = session;
    }

    public boolean isProcessing() {
        return this.detector.queueSize() > 0 || this.matcher.queueSize() > 0 || this.detector.isBusy() || this.matcher.isBusy();
    }

    /**
     * Start processing a session.
     */
    public void startProcessing() {
        this.stopProcessing();
        if (this.current_session == null) {
            logger.log(LoggerLevel.WARNING, "FaceSessionProcessor: No session open. Cannot start processing!");
        } else {
            // faces
            List<Face> faces = faceDao.loadUnprocessed(this.current_session.getId());
            for (Face face: faces) {
                this.matchFace(face);
            }
            // pictures
            List<Picture> pictures = pictureDao.loadUnprocessed(this.current_session.getId());
            for (Picture picture: pictures) {
                this.processPicture(picture);
            }
        }
    }

    /**
     * Stop session processing.
     */
    public void stopProcessing() {
        this.detector.clearQueue();
        this.matcher.clearQueue();
    }
    
    /**
     * Send picture to face detection processing.
     * @param picture
     */
    public void processPicture(Picture picture) {
        assert picture != null;
        logger.log("FaceSessionProcessor: Processing image " + picture.getName());
        this.detectFaces(picture);
    }


    /* FACE DETECTION */

    @Autowired
    private FaceDetector detector;

    public int picturesQueueSize() {
        return this.detector.queueSize();
    }

    private void detectFaces(Picture picture) {
        detector.processPicture(picture);
    }

    /**
     * This is called from FaceDetector when faces in a picture are successfully detected.
     * Successfully means that there were no errors or exceptions, not that any faces were detected.
     * @param result
     */
    @Transactional
    protected void onFacesDetected(FaceDetectionResult result) {
        if (result.is_ok) {
            if (result.picture != null && result.picture.getFaces() != null) {
                logger.log("FaceSessionProcessor: Image face detection of %s completed successfully.", result.picture.toString());
                for (Face face : result.picture.getFaces()) {
                    faceDao.save(face);
                    this.matchFace(face);
                }
                result.picture.setProcessed(true);
                pictureDao.save(result.picture);

                mainWindowController.onPictureProcessed(result.picture);
            } else {
                logger.log(LoggerLevel.ERROR,"FaceSessionProcessor: Image face detection of %s did not return faces collection.", result.picture == null ? "NULL" : result.picture.toString());
            }
        } else {
            logger.log(LoggerLevel.ERROR, "FaceSessionProcessor: Image face detection of %s failed with error: %s", result.picture == null ? "NULL" : result.picture.toString(), result.error_message);
        }
    }
    
    /* FACE MATCHING */

    @Autowired
    private FaceMatcher matcher;

    public int facesQueueSize() {
        return this.matcher.queueSize();
    }

    /**
     * Send face to face matching processing.
     * @param face
     */
    public void matchFace(Face face) {
        matcher.processFace(face);
    }

    /**
     * This is called from FaceMatcher submodule when face matching against all others is successfully finished.
     * @param result
     */
    void onFaceMatched(FaceMatchingResult result) {
        if (result.is_ok) {
            if (result.face != null) {
                logger.log("FaceSessionProcessor: Image face matching of %s completed successfully.", result.face.toString());
                result.face.setProcessed(true);
                faceDao.save(result.face);
                mainWindowController.onFaceProcessed(result.face);
            } else {
                logger.log(LoggerLevel.ERROR,"FaceSessionProcessor: Image face matching did not return face.");
            }
        } else {
            logger.log(LoggerLevel.ERROR, "FaceSessionProcessor: Image face matching of %s failed with error: %s", result.face == null ? "NULL" : result.face.toString(), result.error_message);
        }
    }

}
