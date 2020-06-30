package eu.zavadil.prototype1.face.mock;

import eu.zavadil.prototype1.face.facepp.FaceppAPIService;
import eu.zavadil.prototype1.face.FaceMatcherProviderInterface;
import eu.zavadil.prototype1.FaceMatchingResult;
import eu.zavadil.prototype1.persistence.model.Face;
import eu.zavadil.prototype1.persistence.model.CountingSession;

/**
 * Face++ web API provider for face matching.
 */
public class MockFaceMatcherProvider extends FaceppAPIService implements FaceMatcherProviderInterface {

    @Override
    public FaceMatchingResult matchFace(Face face, CountingSession countingSession) {
        
        FaceMatchingResult result = new FaceMatchingResult(face);
                    
        if (Math.random() > 0.5) {
            face.picture.faces_matched.add(face);
            face.matched = true;
        } else {
            face.picture.faces_unmatched.add(face);
            face.matched = false;
        }            

        return result;
    }
        
}
