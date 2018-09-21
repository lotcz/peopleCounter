package eu.zavadil.prototype1.facepp;

import eu.zavadil.prototype1.FaceDetectionResult;
import eu.zavadil.prototype1.FaceDetectorProviderInterface;
import eu.zavadil.prototype1.model.Face;
import eu.zavadil.prototype1.model.Picture;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Face++ web API provider for face detection.
 */
public class FaceppFaceDetectorProvider implements FaceDetectorProviderInterface {

    @Override
    public FaceDetectionResult detectFaces(Picture picture) {
        FaceDetectionResult result = new FaceDetectionResult(picture);

        JSONObject response_json;
        FaceppAPIService api = new FaceppAPIService();
        api.addFileParam("image_file", picture.path);
        try {
            response_json = api.callMethod("detect");
            JSONArray faces_array = response_json.getJSONArray("faces");
                        
            for (int i = 0, max = faces_array.length(); i < max; i++) {
                JSONObject face_object = faces_array.getJSONObject(i);
                String face_name = picture.name + "." + i;
                result.picture.faces_detected.add(new Face(result.picture, face_name, face_object.getString("face_token")));
            }

        } catch (Exception e) {
            result.is_ok = false;
            result.error_message = e.toString();
        }
        
        return result;
    }
}
