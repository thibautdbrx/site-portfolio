package org.tibocv.apicv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // Ex: "Langage", "Sécurité Offensive", "Réseau", "DevSecOps"
    private String category;

    // Ex: "Avancé", "Intermédiaire" ou un chiffre 1-5
    private String level;

    private String iconUrl; // Lien vers un SVG ou une icône FontAwesome
}
