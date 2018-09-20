package eu.zavadil.prototype1;

import eu.zavadil.prototype1.model.Picture;

/**
 * Interface for FaceDetector providers.
 * Regardless of actual face detection method, all providers must implement this interface.
 */
public interface FaceDetectorProviderInterface {
    
    public FaceDetectionResult detectFaces(Picture item);
    
}
