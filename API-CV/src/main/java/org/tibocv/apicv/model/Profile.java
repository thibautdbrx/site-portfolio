package org.tibocv.apicv.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private String firstName;
    private String lastName;
    private String title; // ex: "Étudiant Ingénieur en Cybersécurité"

    @Column(columnDefinition = "TEXT")
    private String bio;

    private String email;
    private String location;

    private String firstLanguage;
    private String secondLanguage;

    private String phone;

    private String avatarUrl;

    // Liens vers tes réseaux/plateformes cyber
    private String githubUrl;
    private String linkedinUrl;
    private String tryHackMeUrl;
    private String cvPdfUrl;


}
