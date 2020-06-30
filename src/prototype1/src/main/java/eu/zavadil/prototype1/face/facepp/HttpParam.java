package eu.zavadil.prototype1.face.facepp;

/**
 * A single http POST parameter.
 */
public class HttpParam {
    public String name;
    public String value;
    public HttpParamType type = HttpParamType.TEXT;
    
    public HttpParam(String param_name, String param_value) {
        this(param_name, param_value, HttpParamType.TEXT);
    }
    
    public HttpParam(String param_name, String param_value, HttpParamType param_type) {
        name = param_name;
        value = param_value;
        type = param_type;
    }
}
