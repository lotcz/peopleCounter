package eu.zavadil.prototype1;

import eu.zavadil.prototype1.persistence.model.Picture;

/**
 * Result of face detection.
 */
public class FaceDetectionResult {
    
    public boolean is_ok = true;
    
    public Picture picture;
    
    public String error_message;
    
    public FaceDetectionResult(Picture analyzed_picture) {
        picture = analyzed_picture;
    }
}