package eu.zavadil.prototype1;

import java.util.LinkedList;

/**
 *
 * @author karel
 */
public class PicturesQueue extends LinkedList<PictureItem> {
        
    public void enqueue(PictureItem picture) {
        add(picture);
    }
    
    public PictureItem dequeue() {
        return pollFirst();
    }
       
}
