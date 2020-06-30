package eu.zavadil.peopleCounter.face;

import eu.zavadil.peopleCounter.face.FaceMatchingResult;
import eu.zavadil.peopleCounter.persistence.model.Face;
import eu.zavadil.peopleCounter.persistence.model.Session;

import java.util.List;

/**
 * Interface for FaceMatcher providers.
 * Regardless of actual face matching method, all providers must implement this interface.
 */
public interface FaceMatcherProvider {
    
    FaceMatchingResult matchFace(Face face, List<Face> existing_faces);
    
}