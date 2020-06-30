package eu.zavadil.prototype1.face.mock;

import eu.zavadil.prototype1.face.facepp.FaceppAPIService;
import eu.zavadil.prototype1.FaceDetectionResult;
import eu.zavadil.prototype1.face.FaceDetectorProviderInterface;
import eu.zavadil.prototype1.persistence.model.Face;
import eu.zavadil.prototype1.persistence.model.Picture;

/**
 * Face++ web API provider for face detection.
 */
public class MockFaceDetectorProvider extends FaceppAPIService implements FaceDetectorProviderInterface {

    @Override
    public FaceDetectionResult detectFaces(Picture picture) {        
        FaceDetectionResult result = new FaceDetectionResult(picture);
        result.is_ok = true;
        result.picture.faces_detected.add(new Face(picture, "test", "test"));
        return result;
    }
}
