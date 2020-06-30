package eu.zavadil.prototype1.persistence.model;

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
