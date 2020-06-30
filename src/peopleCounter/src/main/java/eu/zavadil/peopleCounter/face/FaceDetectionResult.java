package eu.zavadil.peopleCounter.face;

import eu.zavadil.peopleCounter.persistence.model.Face;
import eu.zavadil.peopleCounter.persistence.model.Picture;

import java.util.List;

/**
 * Result of face detection.
 */
public class FaceDetectionResult {
    
    public boolean is_ok = true;
    
    public Picture picture;

    public List<Face> faces;
    
    public String error_message;
    
    public FaceDetectionResult(Picture analyzed_picture) {
        picture = analyzed_picture;
    }
}