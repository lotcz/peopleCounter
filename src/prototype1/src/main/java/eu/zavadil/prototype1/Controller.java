package eu.zavadil.prototype1;

/**
 * Core of peopleCounter.
 */
public class Controller {
    
    Controller() {
        
    }
    
    public void processPicture(PictureItem picture) {
        System.out.println("Controller: Processing image " + picture );
        processDetectionQueue(picture);
    }
    
    /* FACE DETECTION */
    
    private FaceDetector detector;
    private final PicturesQueue detection_queue = new PicturesQueue();
    
    private void processDetectionQueue(PictureItem picture) {
        if (detector == null) {
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
    
    void detectFaces(PictureItem picture) {               
        
        Runnable on_success = new Runnable() {
            
            @Override
            public void run() {
                detector = null;
                onFacesDetected(picture);
            }

        };
        
        Runnable on_error = new Runnable() {
            
            @Override
            public void run() {    
                detector = null;
                onFacesDetectionError(picture);
            }

        };
        
        detector = new FaceDetector(picture, on_success, on_error);
        detector.start();
        
    }
        
    void onFacesDetected(PictureItem picture) {
        System.out.println("Controller: Image face detection completed " + picture );
        processMatchingQueue(picture);
        processDetectionQueue(null);
    }
    
    void onFacesDetectionError(PictureItem picture) {
        System.out.println("Controller: Image face detection failed " + picture );
        processDetectionQueue(null);
    }
    
    /* FACE MATCHING */
    
    private final PicturesQueue matching_queue = new PicturesQueue();
    private FaceMatcher matcher;
    
    private void processMatchingQueue(PictureItem picture) {
        if (matcher == null) {
            if (!matching_queue.isEmpty()) {
                if (picture != null) {
                    matching_queue.enqueue(picture);
                }
                picture = matching_queue.dequeue();
            }
            if (picture != null) {
                matchFaces(picture);
            }
        } else {
            if (picture != null) {
                matching_queue.enqueue(picture);
            }
        }
    }
    
    void matchFaces(PictureItem picture) {
        Runnable on_success = new Runnable() {
            
            @Override
            public void run() {
                matcher = null;
                onFacesMatched(picture);
            }

        };
        
        Runnable on_error = new Runnable() {
            
            @Override
            public void run() {
                matcher = null;
                onFaceMatchingError(picture);
            }

        };
        
        matcher = new FaceMatcher(picture, on_success, on_error);
        matcher.start();
    }
    
    void onFacesMatched(PictureItem picture) {
        System.out.println("Controller: Image face matching completed " + picture );
        processMatchingQueue(null);
    }
    
    void onFaceMatchingError(PictureItem picture) {
        System.out.println("Controller: Image face matching failed " + picture );
        processMatchingQueue(null);
    }
}
