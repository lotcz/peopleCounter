package eu.zavadil.prototype1.model;

/**
 * Represents a single face found in a picture.
 */
public class Face {
    public final Picture picture;
    public String name;
    public String token;
    
    public Face(Picture source_picture, String face_name, String face_token) {
        picture = source_picture;
        name = face_name;
        token = face_token;        
    }
    
}
