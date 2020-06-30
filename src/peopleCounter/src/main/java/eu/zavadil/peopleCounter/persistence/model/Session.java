package eu.zavadil.peopleCounter.persistence.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a single people counting session.
 */
@Entity
@Table
@Data
public class Session {

    @Id
    @GeneratedValue(generator="increment")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    private Date createDate = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @NotBlank
    private String name;

    @NotBlank
    private String path;

}
