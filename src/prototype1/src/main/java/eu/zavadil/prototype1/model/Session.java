package eu.zavadil.prototype1.model;

/**
 * Represents a single people counting session.
 */
public class Session {
    
    /**
     * All pictures captured and analyzed in this session.
     */
    public PictureCollection pictures = new PictureCollection();
    
    /**
     * All unique faces found in this session.
     */
    public FaceCollection unique_faces = new FaceCollection();
    
    
    public Session() {
        
    }
}
