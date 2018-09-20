package eu.zavadil.prototype1.mock;

import eu.zavadil.prototype1.facepp.*;
import eu.zavadil.prototype1.FaceMatcherProviderInterface;
import eu.zavadil.prototype1.FaceMatchingResult;
import eu.zavadil.prototype1.model.Face;
import eu.zavadil.prototype1.model.Session;

/**
 * Face++ web API provider for face matching.
 */
public class MockFaceMatcherProvider extends FaceppAPIService implements FaceMatcherProviderInterface {

    @Override
    public FaceMatchingResult matchFace(Face face, Session session) {
        
        FaceMatchingResult result = new FaceMatchingResult(face);
                    
        if (Math.random() > 0.5) {
            face.picture.faces_matched.add(face);
            result.face_match = true;
        } else {
            face.picture.faces_unmatched.add(face);
            result.face_match = false;
        }            

        return result;
    }
        
}
