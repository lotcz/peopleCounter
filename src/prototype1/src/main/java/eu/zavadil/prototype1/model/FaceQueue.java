package eu.zavadil.prototype1.model;

/**
 * Queue of faces waiting for processing.
 */
public class FaceQueue extends FaceCollection {
        
    public void enqueue(Face face) {
        add(face);
    }
    
    public Face dequeue() {
        return pollFirst();
    }
       
}
