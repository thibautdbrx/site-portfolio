package org.tibocv.apicv.dto;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String title;
    private String summary;
    private String fullDescription;
    private String securityFocus;
    private List<String> technologies;
    private String githubUrl;
    private String demoUrl;
    private String imageUrl;
}
