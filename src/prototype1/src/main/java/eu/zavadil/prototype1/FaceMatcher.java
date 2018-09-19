package eu.zavadil.prototype1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Face matcher runner.
 */
public class FaceMatcher implements Runnable {
    private Thread t;
    private final PictureItem picture;
    private final Runnable on_finished_runnable;
    private final Runnable on_error_runnable;
    
    public final FacesCollection existing_faces;
    
    FaceMatcher(PictureItem item, FacesCollection faces, Runnable on_finished, Runnable on_error) {
      picture = item;
      on_finished_runnable = on_finished;
      on_error_runnable = on_error;
      existing_faces = faces;
   }
   
   public void start () {
      //System.out.println("FaceMatcher: Starting face matching " + this.picture );
      if (t == null) {
         t = new Thread(this);
         t.start ();
      }
   }
   
   @Override
   public void run() {      
      try {         

        boolean face_matched;
        
        for(PictureFace face: picture.faces_detected) {
            face_matched = false;
            for(PictureFace existing_face: existing_faces) {
                if (facesMatch(face, existing_face)) {
                    face_matched = true;
                    break;
                }
            }
            if (face_matched) {
                picture.faces_matched.add(face);
            } else {
                picture.faces_unmatched.add(face);
            }
        }
        
        if (on_finished_runnable != null) {
            on_finished_runnable.run();                 
        }
       
          
      } catch (Exception e) {
         System.out.println("FaceMatcher: Error " + e);
         if (on_error_runnable != null) {
            on_error_runnable.run();                 
        }
      }
      
   } 
   
   private boolean facesMatch(PictureFace face1, PictureFace face2) throws Exception {
       
       boolean faces_match = false;
       
       /* MATCHING */
        String api_key = "Rl2-GJWPT_r2L1lF_MhiA_cFab7ScOwD";
        String api_secret = "eyOK0hXlUxP4hDgojx4pudAVW41vDZSE";            
        String url = "https://api-us.faceplusplus.com/facepp/v3/compare";

        URL obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        String boundary = Long.toHexString(System.currentTimeMillis());
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        String CRLF = "\r\n";
        String charset = "UTF-8";

        // Send post request            
        DataOutputStream output = new DataOutputStream(con.getOutputStream());
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);

        // Send normal param.
        writer.append("--" + boundary).append(CRLF);
        writer.append("Content-Disposition: form-data; name=\"api_key\"").append(CRLF);
        writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
        writer.append(CRLF).append(api_key).append(CRLF).flush();

        writer.append("--" + boundary).append(CRLF);
        writer.append("Content-Disposition: form-data; name=\"api_secret\"").append(CRLF);
        writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
        writer.append(CRLF).append(api_secret).append(CRLF).flush();

        writer.append("--" + boundary).append(CRLF);
        writer.append("Content-Disposition: form-data; name=\"face_token1\"").append(CRLF);
        writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
        writer.append(CRLF).append(face1.token).append(CRLF).flush();

        writer.append("--" + boundary).append(CRLF);
        writer.append("Content-Disposition: form-data; name=\"face_token2\"").append(CRLF);
        writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
        writer.append(CRLF).append(face2.token).append(CRLF).flush();
        
        // End of multipart/form-data.
        writer.append("--" + boundary + "--").append(CRLF).flush();

        System.out.println("\nSending 'POST' request to URL : " + url);
        //System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + con.getResponseCode());
        System.out.println("Response Message : " + con.getResponseMessage());

        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject response_json = new JSONObject(response.toString());                
            //System.out.println(response_json);
            //System.out.println(response_json.getInt("time_used"));
            
            float confidence = response_json.getFloat("confidence");
            
            if (confidence > 70) {
                faces_match = true;
            }

        } catch (Exception e) {
            BufferedReader ine = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String error_line;
            StringBuffer error_message = new StringBuffer();

            while ((error_line = ine.readLine()) != null) {
                error_message.append(error_line);
            }
            ine.close();

            System.out.println("Response Error : " + error_message);
        }
        
        return faces_match;
   }
}
