package eu.zavadil.prototype1;

/**
 * A single picture for processing.
 */
public class PictureItem {
    
    public String path;
    public int faces_detected = 0;
    public int faces_matched = 0;
    
    PictureItem(String image_path) {
        path = image_path;
    }
    
    @Override
    public String toString() {
        return String.format("PictureItem [%s] DETECTED: %d, MATCHED: %d", path, faces_detected, faces_matched);
    }
    
}
