package eu.zavadil.prototype1.face;

import eu.zavadil.prototype1.FaceDetectionResult;
import eu.zavadil.prototype1.persistence.model.Picture;

/**
 * Interface for FaceDetector providers.
 * Regardless of actual face detection method, all providers must implement this interface.
 */
public interface FaceDetectorProviderInterface {
    
    public FaceDetectionResult detectFaces(Picture item);
    
}
