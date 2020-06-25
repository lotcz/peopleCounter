package eu.zavadil.prototype1.facepp;

import eu.zavadil.prototype1.FaceMatcherProviderInterface;
import eu.zavadil.prototype1.FaceMatchingResult;
import eu.zavadil.prototype1.model.Face;
import eu.zavadil.prototype1.model.Session;
import org.json.JSONObject;

/**
 * Face++ web API provider for face matching.
 */
public class FaceppFaceMatcherProvider implements FaceMatcherProviderInterface {

    /**
     * Default value of face_match.
     * This value will be used when there was an error in processing.
     */
    private boolean face_match_by_default = false;
    
    /**
     * Face is considered matching if confidence is equal or higher than this.
     */
    private float min_confidence_to_match = 75;
    
    @Override
    public FaceMatchingResult matchFace(Face face, Session session) {
        
        FaceMatchingResult result = new FaceMatchingResult(face);
                
        try {
            boolean face_matched = face_match_by_default;
        
            for(Face existing_face: session.unique_faces) {
                try {
                    if (facesMatch(face, existing_face) >= min_confidence_to_match) {
                        face_matched = true;
                        break;
                    }
                } catch (Exception e) {
                    System.out.println("Face matching ended with error: " + e);
                }
            }
            
            if (face_matched) {
                face.picture.faces_matched.add(face);
            } else {
                face.picture.faces_unmatched.add(face);
            }            

        } catch (Exception e) {
            result.is_ok = false;
            result.error_message = e.getMessage();
        }
        
        return result;
    }
    
    /**
     * Compares two faces and return confidence that those two faces belong to same person.
     * @param face1
     * @param face2
     * @return
     * @throws Exception 
     */
    private float facesMatch(Face face1, Face face2) throws Exception {
        JSONObject response_json;
        FaceppAPIService api = new FaceppAPIService();
        api.addTextParam("face_token1", face1.token);
        api.addTextParam("face_token2", face2.token);
        response_json = api.callMethod("compare");        
        return response_json.getFloat("confidence");
    }
}
