package eu.zavadil.peopleCounter.face.mock;

import eu.zavadil.peopleCounter.face.FaceMatcherProvider;
import eu.zavadil.peopleCounter.face.FaceMatchingResult;
import eu.zavadil.peopleCounter.persistence.model.Face;

import java.util.List;

/**
 * Face++ web API provider for face matching.
 */
public class MockFaceMatcherProvider implements FaceMatcherProvider {

    @Override
    public FaceMatchingResult matchFace(Face face, List<Face> existing) {
        FaceMatchingResult result = new FaceMatchingResult(face);
        if (Math.random() > 0.5) {
            face.setMatched(true);
        } else {
            face.setMatched(false);
        }
        return result;
    }
        
}
