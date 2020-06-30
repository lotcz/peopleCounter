package eu.zavadil.peopleCounter.face.facepp;

import eu.zavadil.peopleCounter.core.MessageBuilder;
import eu.zavadil.peopleCounter.face.FaceMatcherProvider;
import eu.zavadil.peopleCounter.face.FaceMatchingResult;
import eu.zavadil.peopleCounter.persistence.model.Face;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Face++ web API provider for face matching.
 */
@Component
public class FaceppFaceMatcherProvider implements FaceMatcherProvider {

    /**
     * Face is considered matching if confidence is equal or higher than this.
     */
    private float min_confidence_to_match = 60;
    
    @Override
    public FaceMatchingResult matchFace(Face face, List<Face> existing) {
        FaceMatchingResult result = new FaceMatchingResult(face);
        try {
            boolean face_matched = false;
            for(Face existing_face: existing) {
                if (facesMatch(face, existing_face) >= min_confidence_to_match) {
                    face_matched = true;
                    break;
                }
            }
            face.setMatched(face_matched);
        } catch (Exception e) {
            result.is_ok = false;
            result.error_message = MessageBuilder.buildExceptionMessage(e);
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
        api.addTextParam("face_token1", face1.getToken());
        api.addTextParam("face_token2", face2.getToken());
        response_json = api.callMethod("compare");
        float confidence = response_json.getFloat("confidence");
        System.out.println("Confidence:" + String.valueOf(confidence));
        return confidence;
    }
}
