package eu.zavadil.prototype1;

import eu.zavadil.prototype1.facepp.FaceppFaceMatcherProvider;
import eu.zavadil.prototype1.mock.MockFaceMatcherProvider;
import eu.zavadil.prototype1.model.Face;
import eu.zavadil.prototype1.model.FaceQueue;

/**
 * Face matcher runner.
 */
public class FaceMatcher {
    
    private final Controller controller;
    private final FaceMatcherSettings settings;
    private final FaceMatcherProviderInterface provider;    
    
    private Thread matching_thread = null;
        
    FaceMatcher(Controller main_controller, FaceMatcherSettings matcher_settings) {
        controller = main_controller;
        settings = matcher_settings;
      
        /* create provider based on settings. */
        switch (settings.face_matching_provider) {
            case FACEPP:
                provider = new FaceppFaceMatcherProvider();
                break;
            default:
                provider = new MockFaceMatcherProvider();
        }
        
    }
   
    /**
     * Queues face for face matching.
     * @param face 
     */
    public void processFace(Face face) {
        processMatchingQueue(face);
    }
  
    /**
     * Returns true if detector is busy.
     * Detecting thread can only be created when detector is not busy.
     * @return boolean
     */
    public boolean isBusy() {
        return (matching_thread != null);
    }
    
    /* MATCHING QUEUE */
    
    private final FaceQueue matching_queue = new FaceQueue();
    
    /**
     * If a face is provided, it will be added to the queue. 
     * If face matcher is not busy, next item from queue will be processed.
     * @param picture 
     */
    private void processMatchingQueue(Face face) {
        if (!isBusy()) {
            if (!matching_queue.isEmpty()) {
                if (face != null) {
                    matching_queue.enqueue(face);
                }
                face = matching_queue.dequeue();
            }
            if (face != null) {
                matchFace(face);
            }
        } else {
            if (face != null) {
                matching_queue.enqueue(face);
            }
        }
    }
    
    private void matchFace(Face face) {    
        
        if (isBusy()) {
            reportError("Matcher is busy. New matching thread cannot be created. Resending face to queue.");
            processMatchingQueue(face);
        } else {
                    
            Runnable matching_job = new Runnable() {
                @Override
                public void run() {                 
                    matchingComplete(provider.matchFace(face, controller.getCurrentSession()));                    
                }
            };

            matching_thread = new Thread(matching_job);
            matching_thread.start();
        }
              
   } 
   
    private void matchingComplete(FaceMatchingResult result) {
        matching_thread = null;
        if (result.is_ok) {
            if (!result.face_match) {               
                controller.getCurrentSession().unique_faces.add(result.face);
            }
            controller.onFaceMatched(result.face);            
        } else {
            reportError(result.error_message);
        }
        processMatchingQueue(null);
    }
    
    private void reportError(String message) {
        controller.onError("FaceMatcher", message);
    }
   
}
