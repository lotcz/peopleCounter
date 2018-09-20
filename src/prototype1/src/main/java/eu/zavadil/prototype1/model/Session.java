package eu.zavadil.prototype1.model;

/**
 * Represents a single people counting session.
 */
public class Session {
    
    /**
     * All unique faces found in this session.
     */
    public FaceCollection unique_faces = new FaceCollection();
    
    
    public Session() {
        
    }
}
