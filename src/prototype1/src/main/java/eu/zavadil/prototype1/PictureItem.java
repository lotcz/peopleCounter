package eu.zavadil.prototype1;

/**
 * A single picture for processing.
 */
public class PictureItem {
    
    public String path;
    public FacesCollection faces_detected = new FacesCollection();
    public FacesCollection faces_matched = new FacesCollection();
    public FacesCollection faces_unmatched = new FacesCollection();
    
    PictureItem(String image_path) {
        path = image_path;
    }
    
    @Override
    public String toString() {
        return String.format("PictureItem [%s] DETECTED: %d, UNMATCHED: %d", path, faces_detected.size(), faces_unmatched.size());
    }
    
}
