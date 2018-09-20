package eu.zavadil.prototype1;

import eu.zavadil.prototype1.facepp.FaceppFaceDetectorProvider;
import eu.zavadil.prototype1.mock.MockFaceDetectorProvider;
import eu.zavadil.prototype1.model.*;

/**
 * Face detector runner.
 */
public class FaceDetector {
    
    private final Controller controller;
    private final FaceDetectorSettings settings;
    private final FaceDetectorProviderInterface provider;    

    private Thread detection_thread = null;
    
    FaceDetector(Controller main_controller, FaceDetectorSettings detector_settings) {
        controller = main_controller;
        settings = detector_settings;
        
        /* create provider based on settings. */
        switch (settings.face_detection_provider) {
            case FACEPP:
                provider = new FaceppFaceDetectorProvider();
                break;
            default:
                provider = new MockFaceDetectorProvider();
        }
        
    }
   
    /**
     * Queues picture for face detection.
     * @param picture 
     */
    public void processPicture(Picture picture) {
        processDetectionQueue(picture);
    }
    
    /**
     * Returns true if detector is busy.
     * Detecting thread can only be created when detector is not busy.
     * @return boolean
     */
    public boolean isBusy() {
        return (detection_thread != null);
    }
    
    /* DETECTION QUEUE */
    
    private final PictureQueue detection_queue = new PictureQueue();

    /**
     * If a picture is provided, it will be added to the queue. 
     * If face detector is not busy, next item from queue will be processed.
     * @param picture 
     */
    private void processDetectionQueue(Picture picture) {
        if (!isBusy()) {
            if (!detection_queue.isEmpty()) {
                if (picture != null) {
                    detection_queue.enqueue(picture);
                }
                picture = detection_queue.dequeue();
            }
            if (picture != null) {
                detectFaces(picture);
            }
        } else {
            if (picture != null) {
                detection_queue.enqueue(picture);
            }
        }
    }
   
    /**
     * Will create a thread with face detection.
     * If face detector is busy, an exception will be raised.
     * @param picture Must be instance of Picture.
     */
    private void detectFaces(Picture picture) {
        
        if (isBusy()) {
            reportError("Detector is busy. New detecting thread cannot be created. Resending picture to queue.");
            processDetectionQueue(picture);
        } else {
        
            final Picture detection_picture = picture;
            Runnable detection_job = new Runnable() {
                @Override
                public void run() {                 
                    detectionComplete(provider.detectFaces(detection_picture));
                }
            };

            detection_thread = new Thread(detection_job);
            detection_thread.start ();
        }
        
   }
   
    private void detectionComplete(FaceDetectionResult result) {
        detection_thread = null;
        if (result.is_ok) {
            controller.onFacesDetected(result.picture);
        } else {
            reportError(result.error_message);
        }
        processDetectionQueue(null);
    }
   
    private void reportError(String message) {
        controller.onError("FaceDetector", message);
    }
}
