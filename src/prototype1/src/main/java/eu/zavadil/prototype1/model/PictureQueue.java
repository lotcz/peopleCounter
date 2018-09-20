package eu.zavadil.prototype1.model;

/**
 * Queue of pictures waiting for processing.
 */
public class PictureQueue extends PictureCollection {
        
    public void enqueue(Picture picture) {
        add(picture);
    }
    
    public Picture dequeue() {
        return pollFirst();
    }
       
}
