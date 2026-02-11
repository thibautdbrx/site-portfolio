package org.tibocv.apicv.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter @NoArgsConstructor @AllArgsConstructor
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String companyName;
    private String position;
    private String location;

    private String startDate; // Utiliser String pour "Sept. 2023" ou LocalDate
    private String endDate;   // "Présent" ou une date

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    private List<String> toolsUsed; // Outils spécifiques (ex: Wireshark, Burp, Docker)
}
