package eu.zavadil.prototype1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import org.json.*;

/**
 * Face detector runner.
 */
public class FaceDetector implements Runnable {
    private Thread t;
    private final PictureItem picture;
    private Runnable on_finished_runnable;
    private Runnable on_error_runnable;
    
    FaceDetector(PictureItem item, Runnable on_finished, Runnable on_error) {
      picture = item;
      on_finished_runnable = on_finished;
      on_error_runnable = on_error;
   }
   
   public void start () {
      //System.out.println("FaceDetector: Starting detection " + this.picture );
      if (t == null) {
         t = new Thread(this);
         t.start ();
      }
   }
   
   @Override
   public void run() {      
        try {         
            /* DETECTION */
            String api_key = "Rl2-GJWPT_r2L1lF_MhiA_cFab7ScOwD";
            String api_secret = "eyOK0hXlUxP4hDgojx4pudAVW41vDZSE";            
            String url = "https://api-us.faceplusplus.com/facepp/v3/detect";
            
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

            File image_file = new File(picture.path);
                        
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"image_file\"; filename=\"" + image_file.getName() + "\"").append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(image_file.getName())).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            Files.copy(image_file.toPath(), output);
            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();
    
            //System.out.println("\nSending 'POST' request to URL : " + url);
            //System.out.println("Post parameters : " + urlParameters);
            //System.out.println("Response Code : " + con.getResponseCode());
            //System.out.println("Response Message : " + con.getResponseMessage());
            
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
                picture.faces_detected = response_json.getJSONArray("faces").length();
                
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
            
            if (on_finished_runnable != null) {
                on_finished_runnable.run();                 
            }
        } catch (Exception e) {
           System.out.println("FaceDetector: Error " + e);
           if (on_error_runnable != null) {
                on_error_runnable.run();                 
            }
        }        
   } 
   
}
