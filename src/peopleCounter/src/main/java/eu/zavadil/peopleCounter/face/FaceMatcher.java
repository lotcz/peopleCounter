package eu.zavadil.peopleCounter.face;

import eu.zavadil.peopleCounter.core.Logger;
import eu.zavadil.peopleCounter.persistence.dao.FaceDao;
import eu.zavadil.peopleCounter.persistence.model.Face;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Face matcher runner.
 */
@Component
public class FaceMatcher {

    @Autowired
    private FaceSessionProcessor sessionProcessor;

    @Autowired
    private FaceMatcherProvider provider;

    @Autowired
    private Logger logger;

    @Autowired
    private FaceDao faceDao;

    private Thread matching_thread = null;

    /**
     * Queues face for face matching.
     * @param face 
     */
    protected void processFace(Face face) {
        processMatchingQueue(face);
    }
  
    /**
     * Returns true if detector is busy.
     * Detecting thread can only be created when detector is not busy.
     * @return boolean
     */
    public boolean isBusy() {
        return (matching_thread != null);
    }
    
    /* MATCHING QUEUE */
    
    private final Queue<Face> matching_queue = new LinkedList<>();

    public int queueSize() {
        return this.matching_queue.size();
    }

    /**
     * If a face is provided, it will be added to the queue. 
     * If face matcher is not busy, next item from queue will be processed.
     * @param face
     */
    private void processMatchingQueue(Face face) {
        if (!isBusy()) {
            if (!matching_queue.isEmpty()) {
                if (face != null) {
                    matching_queue.add(face);
                }
                face = matching_queue.poll();
            }
            if (face != null) {
                matchFace(face);
            }
        } else {
            if (face != null) {
                matching_queue.add(face);
            }
        }
    }

    public void clearQueue() {
        this.matching_queue.clear();
    }

    private void matchFace(Face face) {
        if (isBusy()) {
            processMatchingQueue(face);
        } else {
            final Face matching_face = face;
            final List<Face> matching_existing = faceDao.loadFacesToMatchAgainst(face);
            Runnable matching_job = new Runnable() {
                @Override
                public void run() {                 
                    matchingComplete(provider.matchFace(matching_face, matching_existing));
                }
            };
            matching_thread = new Thread(matching_job);
            matching_thread.start();
        }
   } 
   
    private void matchingComplete(FaceMatchingResult result) {
        matching_thread = null;
        processMatchingQueue(null);
        sessionProcessor.onFaceMatched(result);
    }

}
