package org.tibocv.apicv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.info.Info;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    private String summary;

    @Column(columnDefinition = "TEXT")
    private String fullDescription;

    // Champ spécifique pour briller en cyber
    @Column(columnDefinition = "TEXT")
    private String securityFocus;

    @ElementCollection // Crée automatiquement une table de jointure pour les technos
    private List<String> technologies;

    private String githubUrl;
    private String demoUrl;
    private String imageUrl;

    private boolean isFeatured = false; // Pour mettre en avant sur la home

}