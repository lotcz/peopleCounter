package eu.zavadil.prototype1;

import java.io.File;

import eu.zavadil.prototype1.core.Logger;
import eu.zavadil.prototype1.core.MessageBuilder;
import eu.zavadil.prototype1.face.FaceDetector;
import eu.zavadil.prototype1.face.FaceMatcher;
import eu.zavadil.prototype1.persistence.model.*;

/**
 * Core of peopleCounter. 
 * This central object keeps track of running session and manages processing of all pictures and faces for the session.
 */
public class Controller {
    
    private final ControllerSettings settings;

    private Logger logger;

    /**
     * Will create controller instance with default settings.
     */
    Controller(Logger logger)
    {
        this(new ControllerSettings(), logger);
    }
        
    /**
     * Provide your own settings for Controller.
     * @param controller_settings 
     */
    public Controller(ControllerSettings controller_settings, Logger logger) {
        this.logger = logger;
        this.settings = controller_settings;
        detector = new FaceDetector(this, settings.getFaceDetectorSettings());
        matcher = new FaceMatcher(this, settings.getFaceMatcherSettings());
    }
    
    /* SESSION */
    
    /**
     * Current active session. All new pictures are automatically put here.
     * If there is no session a new pictures are sent for processing, new session is automatically created.
     */
    private CountingSession current_Counting_session;
    
    public CountingSession getCurrentSession() {
        if (current_Counting_session == null) {
            startNewSession();
        }
        return current_Counting_session;
    }
    
    /**
     * Close running session if there is any and return it.
     * @return Closed session.
     */
    public CountingSession closeCurrentSession() {
        CountingSession closed_Counting_session = null;
        if (current_Counting_session != null) {
            closed_Counting_session = current_Counting_session;
            current_Counting_session = null;
        }
        return closed_Counting_session;
    }
    
    /**
     * Resume a session. If there is a session in progress, then it is closed before resuming.
     * @param countingSession Session to be resumed.
     * @return Closed session if there was any.
     */
    public CountingSession resumeSession(CountingSession countingSession) {
        CountingSession closed_Counting_session = closeCurrentSession();
        if (countingSession == null) {
            current_Counting_session = new CountingSession();
        } else {
            current_Counting_session = countingSession;
        }
        return closed_Counting_session;
    }
    
    /**
     * Start a new session. If there is a session in progress, then it is closed and returned.
     * @return Closed session if there was any.
     */
    public CountingSession startNewSession() {
        CountingSession closed_Counting_session = closeCurrentSession();
        current_Counting_session = new CountingSession();
        return closed_Counting_session;
    }
    
    /* PICTURE PROCESSING */

    public void processPicturesFolder(String path) {
        File folder = new File(path);
        File[] files = folder.listFiles();

        for (File file: files) {
            if (file.isFile()) {
                processPictureFile(file.getPath());
            } else if (file.isDirectory()) {
                // TO DO: process subdirectories?
                //System.out.println("Directory " + file.getName());
            }
        }
    }

    public void processPictureFile(String path) {
        processPicture(new Picture(path));
    }

    private void processPicture(Picture picture) {
        logger.log("Controller: Processing image " + picture.toString());
        detectFaces(picture);
    }

    /* FACE DETECTION */
    
    private FaceDetector detector;
    
    /**
     * Send picture to face detection processing.
     * @param picture 
     */
    void detectFaces(Picture picture) {
        detector.processPicture(picture);
    }

    /**
     * This is called from FaceDetector when faces in a picture are successfully detected.
     * Successfully means that there were no errors or exceptions, not that any faces were detected.
     * @param picture 
     */
    void onFacesDetected(Picture picture) {
        logger.log("Controller: Image face detection completed " + picture.toString());
        for (Face face: picture.faces_detected) {            
            matchFace(face);
        }
    }
    
    /* FACE MATCHING */
 
    private FaceMatcher matcher;

    /**
     * Send face to face matching processing.
     * @param picture 
     */
    void matchFace(Face face) {        
        matcher.processFace(face);
    }

    /**
     * This is called from FaceMatcher submodule when face matching for a picture is successfully finished.
     * @param picture 
     */
    void onFaceMatched(Face face) {
        logger.log("Controller: Image face matching completed " + face.toString());
        logger.log("Controller: Unique faces - " + getCurrentSession().unique_faces.size());
    }
    
    /**
     * This is called from any submodule (e.g. FaceDetector) when an error occurs.
     * 
     * @param error_source Name of the source of the error. E.g. name of submodule.
     * @param error_message 
     */
    void onError(String error_source, String error_message) {
        MessageBuilder builder = new MessageBuilder();
        builder.appendLine("ERROR:");
        builder.appendLine("------");
        builder.appendLine("Source", error_source);
        builder.appendLine("Message:");
        builder.appendLine(error_message);
        logger.log(builder.toString());
    }
    
}
