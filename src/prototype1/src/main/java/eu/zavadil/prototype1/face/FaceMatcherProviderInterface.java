package eu.zavadil.prototype1.face;

import eu.zavadil.prototype1.FaceMatchingResult;
import eu.zavadil.prototype1.persistence.model.Face;
import eu.zavadil.prototype1.persistence.model.CountingSession;

/**
 * Interface for FaceMatcher providers.
 * Regardless of actual face matching method, all providers must implement this interface.
 */
public interface FaceMatcherProviderInterface {
    
    public FaceMatchingResult matchFace(Face face, CountingSession countingSession);
    
}