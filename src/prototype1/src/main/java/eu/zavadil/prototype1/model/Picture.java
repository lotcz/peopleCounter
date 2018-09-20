package eu.zavadil.prototype1.model;

/**
 * A single picture/image for processing.
 */
public class Picture {
    
    public String path;
    public FaceCollection faces_detected = new FaceCollection();
    public FaceCollection faces_matched = new FaceCollection();
    public FaceCollection faces_unmatched = new FaceCollection();
    
    public Picture(String image_path) {
        path = image_path;
    }
    
    @Override
    public String toString() {
        return String.format("Picture [%s] DETECTED: %d, UNMATCHED: %d", path, faces_detected.size(), faces_unmatched.size());
    }
    
}
