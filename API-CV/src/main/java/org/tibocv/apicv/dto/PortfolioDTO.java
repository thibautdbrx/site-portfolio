package org.tibocv.apicv.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDTO {
    private ProfileDTO profile;
    private List<EducationDTO> educations;
    private List<ExperienceDTO> experiences;
    private List<ProjectDTO> projects;
    private List<SkillDTO> skills;
}