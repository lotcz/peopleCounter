package eu.zavadil.peopleCounter.persistence.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Represents a single face found in a picture.
 */
@Entity
@Table
@Data
public class Face {

    @Id
    @GeneratedValue(generator="increment")
    private Long id;

    @ManyToOne
    private Picture picture;

    private String name;

    private String token;

    private boolean matched = true;

    private boolean processed = false;

    @Override
    public String toString() {
        return String.format("Face [%s] MATCHED: %s", name, (matched) ? "YES" : "NO");
    }
    
}
