package eu.zavadil.peopleCounter.face.facepp;

import eu.zavadil.peopleCounter.core.MessageBuilder;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import javax.net.ssl.HttpsURLConnection;

/**
 * Base for all Face++ API service calls.
 */
class FaceppAPIService {
    
    protected String api_key = "Rl2-GJWPT_r2L1lF_MhiA_cFab7ScOwD";
    protected String api_secret = "eyOK0hXlUxP4hDgojx4pudAVW41vDZSE";            
    protected String url_base = "https://api-us.faceplusplus.com/facepp/v3/";

    protected String charset = "UTF-8";
    protected String CRLF = "\r\n";
    
    protected String boundary;
    
    private DataOutputStream output;
    private PrintWriter writer;
                
    protected String getMethodUrl(String url_method) {
        return url_base + url_method;
    }
    
    /* PARAMS */
    
    protected HttpParamCollection http_params = new HttpParamCollection();
    
    protected void resetParams() {
        http_params.clear();
    }
    
    public void addTextParam(String param_name, String param_value) {
        http_params.add(new HttpParam(param_name, param_value));
    }
    
    public void addFileParam(String param_name, String file_path) {
        http_params.add(new HttpParam(param_name, file_path, HttpParamType.FILE));
    }
    
    private void writeTextParam(String param_name, String param_value) {
        writer.append("--" + boundary).append(CRLF);
        writer.append(String.format("Content-Disposition: form-data; name=\"%s\"%s", param_name, CRLF));
        writer.append(String.format("Content-Type: text/plain; charset=%s%s", charset, CRLF));
        writer.append(CRLF).append(param_value).append(CRLF).flush();
    }
    
    private void writeFileParam(String param_name, String file_path) throws IOException {
        File file = new File(file_path);
        String file_name = file.getName();
        writer.append("--" + boundary).append(CRLF);
        writer.append(String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"%s", param_name, file_name, CRLF));
        writer.append(String.format("Content-Type: %s%s", URLConnection.guessContentTypeFromName(file_name), CRLF));
        writer.append("Content-Transfer-Encoding: binary").append(CRLF);
        writer.append(CRLF).flush();
        Files.copy(file.toPath(), output);
        output.flush();
        writer.append(CRLF).flush();        
    }
    
    private void writeParam(HttpParam param) throws IOException {
        switch (param.type) {
            case FILE:
                writeFileParam(param.name, param.value);
                break;
            default:
                writeTextParam(param.name, param.value);
                break;
        }
    }
    
    public JSONObject callMethod(String method_name) throws Exception {
        String url = getMethodUrl(method_name);
        URL url_obj = new URL(url);
        HttpsURLConnection con = (HttpsURLConnection) url_obj.openConnection();        
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        boundary = Long.toHexString(System.currentTimeMillis());
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
 
        output = new DataOutputStream(con.getOutputStream());
        writer = new PrintWriter(new OutputStreamWriter(output, charset), true);

        writeTextParam("api_key", api_key);
        writeTextParam("api_secret", api_secret);

        /* write all custom parameters */
        for (HttpParam param: http_params) {
            writeParam(param);
        }
        
        writer.append("--" + boundary + "--").append(CRLF).flush();
           
        if (con.getResponseMessage().compareTo("OK") == 0) {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject response_json = new JSONObject(response.toString());

            return response_json;
        } else {
            MessageBuilder error_message = new MessageBuilder();
            error_message.appendLine("Face++ HTTP API Error:");
            error_message.appendLine("URL", url);
            error_message.appendLine("Response code", con.getResponseCode());
            error_message.appendLine("Response message", con.getResponseMessage());
            
            error_message.appendLine("Error message:");
            BufferedReader ine = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            String error_line;
            while ((error_line = ine.readLine()) != null) {
                error_message.appendLine(error_line);
            }
            ine.close();

            throw new Exception(error_message.toString());
        }
                
    }
}