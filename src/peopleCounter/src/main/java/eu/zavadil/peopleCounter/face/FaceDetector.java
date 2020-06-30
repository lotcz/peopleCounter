package eu.zavadil.peopleCounter.face;

import eu.zavadil.peopleCounter.core.Logger;
import eu.zavadil.peopleCounter.core.LoggerLevel;
import eu.zavadil.peopleCounter.persistence.model.Face;
import eu.zavadil.peopleCounter.persistence.model.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Face detector runner.
 */
@Component
public class FaceDetector {

    @Autowired
    private FaceSessionProcessor sessionProcessor;

    @Autowired
    private FaceDetectorProvider detectionProvider;

    private Thread detection_thread = null;

    /**
     * Queues picture for face detection.
     * @param picture 
     */
    public void processPicture(Picture picture) {
        this.processDetectionQueue(picture);
    }
    
    /**
     * Returns true if detector is busy.
     * Detecting thread can only be created when detector is not busy.
     * @return boolean
     */
    public boolean isBusy() {
        return (detection_thread != null);
    }
    
    private final Queue<Picture> detection_queue = new LinkedList<Picture>();

    public int queueSize() {
        return this.detection_queue.size();
    }

    /**
     * If a picture is provided, it will be added to the queue. 
     * If face detector is not busy, next item from queue will be processed.
     * @param picture 
     */
    private void processDetectionQueue(Picture picture) {
        if (!isBusy()) {
            if (!detection_queue.isEmpty()) {
                if (picture != null) {
                    detection_queue.add(picture);
                }
                picture = detection_queue.poll();
            }
            if (picture != null) {
                detectFaces(picture);
            }
        } else {
            if (picture != null) {
                detection_queue.add(picture);
            }
        }
    }

    public void clearQueue() {
        this.detection_queue.clear();
    }

    /**
     * Will create a thread with face detection.
     * If face detector is busy, an exception will be raised.
     * @param picture Must be instance of Picture.
     */
    private void detectFaces(Picture picture) {
        if (this.isBusy()) {
            this.processDetectionQueue(picture);
        } else {
            final Picture detection_picture = picture;
            Runnable detection_job = new Runnable() {
                @Override
                public void run() {                 
                    detectionComplete(detectionProvider.detectFaces(detection_picture));
                }
            };
            this.detection_thread = new Thread(detection_job);
            this.detection_thread.start ();
        }
   }
   
    private void detectionComplete(FaceDetectionResult result) {
        this.detection_thread = null;
        this.processDetectionQueue(null);
        this.sessionProcessor.onFacesDetected(result);
    }

}
