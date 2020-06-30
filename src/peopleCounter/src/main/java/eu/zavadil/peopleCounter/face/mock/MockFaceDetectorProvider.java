package eu.zavadil.peopleCounter.face.mock;


import eu.zavadil.peopleCounter.face.FaceDetectionResult;
import eu.zavadil.peopleCounter.face.FaceDetectorProvider;
import eu.zavadil.peopleCounter.persistence.model.Face;
import eu.zavadil.peopleCounter.persistence.model.Picture;

/**
 * Face++ web API provider for face detection.
 */
public class MockFaceDetectorProvider implements FaceDetectorProvider {

    @Override
    public FaceDetectionResult detectFaces(Picture picture) {
        FaceDetectionResult result = new FaceDetectionResult(picture);
        result.is_ok = true;
        Face face = new Face();
        face.setPicture(result.picture);
        face.setName("test");
        face.setToken("test");
        result.picture.getFaces().add(face);
        return result;
    }
}
