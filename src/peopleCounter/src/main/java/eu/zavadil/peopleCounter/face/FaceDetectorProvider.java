package eu.zavadil.peopleCounter.face;

import eu.zavadil.peopleCounter.face.FaceDetectionResult;
import eu.zavadil.peopleCounter.persistence.model.Picture;

/**
 * Interface for FaceDetector providers.
 * Regardless of actual face detection method, all providers must implement this interface.
 */
public interface FaceDetectorProvider {
    
    FaceDetectionResult detectFaces(Picture item);
    
}
