package eu.zavadil.peopleCounter.face.facepp;

import eu.zavadil.peopleCounter.face.FaceDetectionResult;
import eu.zavadil.peopleCounter.face.FaceDetectorProvider;
import eu.zavadil.peopleCounter.persistence.model.Face;
import eu.zavadil.peopleCounter.persistence.model.Picture;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

/**
 * Face++ web API provider for face detection.
 */
@Component
public class FaceppFaceDetectorProvider implements FaceDetectorProvider {

    @Override
    public FaceDetectionResult detectFaces(Picture picture) {
        FaceDetectionResult result = new FaceDetectionResult(picture);

        JSONObject response_json;
        FaceppAPIService api = new FaceppAPIService();
        api.addFileParam("image_file", picture.getPath());
        try {
            response_json = api.callMethod("detect");
            JSONArray faces_array = response_json.getJSONArray("faces");

            for (int i = 0, max = faces_array.length(); i < max; i++) {
                JSONObject face_object = faces_array.getJSONObject(i);
                String face_name = picture.getName() + "." + i;
                Face face = new Face();
                face.setPicture(result.picture);
                face.setName(face_name);
                face.setToken(face_object.getString("face_token"));
                result.picture.getFaces().add(face);
            }

        } catch (Exception e) {
            result.is_ok = false;
            result.error_message = e.toString();
        }
        
        return result;
    }
}
