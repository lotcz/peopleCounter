package eu.zavadil.prototype1;

import eu.zavadil.prototype1.persistence.model.Face;

/**
 * Result of face matching.
 */
public class FaceMatchingResult {
    public boolean is_ok = true;
        
    public final Face face;
    
    public String error_message;
    
    public FaceMatchingResult(Face analyzed_face) {
        face = analyzed_face;
    }
}
