package eu.zavadil.prototype1.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Represents a single people counting session.
 */
@Entity
@Table
@Data
public class CountingSession {

    @Id
    @GeneratedValue(generator="increment")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date endDate;

    /**
     * All pictures captured and analyzed in this session.
     */
    private PictureCollection pictures = new PictureCollection();
    
    /**
     * All unique faces found in this session.
     */
    private FaceCollection unique_faces = new FaceCollection();

}
