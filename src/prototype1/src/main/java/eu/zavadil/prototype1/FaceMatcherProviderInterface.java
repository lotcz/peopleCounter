package eu.zavadil.prototype1;

import eu.zavadil.prototype1.model.Face;
import eu.zavadil.prototype1.model.Session;

/**
 * Interface for FaceMatcher providers.
 * Regardless of actual face matching method, all providers must implement this interface.
 */
public interface FaceMatcherProviderInterface {
    
    public FaceMatchingResult matchFace(Face face, Session session);
    
}