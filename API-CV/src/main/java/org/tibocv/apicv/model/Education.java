package org.tibocv.apicv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
@AllArgsConstructor
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String schoolName;
    private String degree; // ex: "Diplôme d'ingénieur"
    private String fieldOfStudy; // ex: "Informatique"
    private String location;

    private String description;

    private String startDate;
    private String endDate;

    @Column(columnDefinition = "TEXT")
    @ElementCollection
    private List<String> relevantCourses;

    private String logo;
    private String url;
}
