package org.tibocv.apicv.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String title;
    private String bio;
    @Email(message = "Email invalide")
    private String email;
    private String location;
    private String firstLanguage;
    private String secondLanguage;
    private String avatarUrl;
    private String phone;
    private String githubUrl;
    private String linkedinUrl;
    private String tryHackMeUrl;
    private String cvPdfUrl;
}
