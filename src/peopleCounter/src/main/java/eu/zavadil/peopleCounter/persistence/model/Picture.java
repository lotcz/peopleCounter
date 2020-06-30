package eu.zavadil.peopleCounter.persistence.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A single picture/image for processing.
 */
@Entity
@Table
@Data
public class Picture {

    @Id
    @GeneratedValue(generator="increment")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date createDate = new Date();

    @ManyToOne
    private Session session;

    private String name;

    private String path;

    private boolean processed = false;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Face> faces = new ArrayList<>();

    @Override
    public String toString() {
        return String.format("Picture [%s] FACES: %d", name, faces.size());
    }
    
}
