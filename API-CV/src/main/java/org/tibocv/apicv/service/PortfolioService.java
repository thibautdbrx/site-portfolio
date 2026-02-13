package org.tibocv.apicv.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.tibocv.apicv.dto.*;
import org.tibocv.apicv.model.*;
import org.tibocv.apicv.repository.*;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final ProjectRepository projectRepository;
    private final SkillRepository skillRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final ProfileRepository profileRepository;
    // Injecte ici tes autres repositories (Experience, Education, etc.)

    public PortfolioDTO getFullPortfolio() {
        return PortfolioDTO.builder()
                .profile(profileRepository.findAll().stream()
                        .findFirst()
                        .map(this::convertToProfileDto)
                        .orElse(null))
                .projects(projectRepository.findAll().stream()
                        .map(this::convertToProjectDto).collect(Collectors.toList()))
                .skills(skillRepository.findAll().stream()
                        .map(this::convertToSkillDto).collect(Collectors.toList()))
                .educations(educationRepository.findAll().stream()
                        .map(this::convertToEducationDto).collect(Collectors.toList()))
                .experiences(experienceRepository.findAll().stream()
                        .map(this::convertToExperienceDto).collect(Collectors.toList()))
                .build();
    }

    private SkillDTO convertToSkillDto(Skill skill) {
        return SkillDTO.builder()
                .name(skill.getName())
                .category(skill.getCategory())
                .level(skill.getLevel())
                .iconUrl(skill.getIconUrl())
                .build();
    }

    private EducationDTO convertToEducationDto(Education education) {
        return EducationDTO.builder()
                .schoolName(education.getSchoolName())
                .degree(education.getDegree())
                .description(education.getDescription())
                .location(education.getLocation())
                .fieldOfStudy(education.getFieldOfStudy())
                .startDate(education.getStartDate())
                .endDate(education.getEndDate())
                .relevantCourses(education.getRelevantCourses())
                .logo(education.getLogo())
                .url(education.getUrl())
                .build();
    }

    private ExperienceDTO convertToExperienceDto(Experience experience) {
        return ExperienceDTO.builder()
                .companyName(experience.getCompanyName())
                .position(experience.getPosition())
                .location(experience.getLocation())
                .startDate(experience.getStartDate())
                .endDate(experience.getEndDate())
                .description(experience.getDescription())
                .toolsUsed(experience.getToolsUsed())
                .build();
    }

    // Exemple de convertisseur (Entity -> DTO)
    private ProjectDTO convertToProjectDto(Project project) {
        return ProjectDTO.builder()
                .title(project.getTitle())
                .summary(project.getSummary())
                .technologies(project.getTechnologies())
                .githubUrl(project.getGithubUrl())
                .build();
    }

    private ProfileDTO convertToProfileDto(Profile profile) {
        return ProfileDTO.builder()
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .title(profile.getTitle())
                .bio(profile.getBio())
                .email(profile.getEmail())
                .location(profile.getLocation())
                .githubUrl(profile.getGithubUrl())
                .linkedinUrl(profile.getLinkedinUrl())
                .phone(profile.getPhone())
                .tryHackMeUrl(profile.getTryHackMeUrl())
                .cvPdfUrl(profile.getCvPdfUrl())
                .avatarUrl(profile.getAvatarUrl())
                .build();
    }
}