package org.tibocv.apicv.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.tibocv.apicv.dto.ProjectDTO;
import org.tibocv.apicv.model.Project;
import org.tibocv.apicv.repository.ProjectRepository;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project addProject(@Valid ProjectDTO project){
        return convertToProject(new Project(), project);
    }

    public void deleteProject(Long id){projectRepository.deleteById(id);}

    public void updateProject(long id, @Valid ProjectDTO project){
        Project project1 = projectRepository.findById(id).orElse(null);
        if (project1 == null) return;
        convertToProject(project1, project);
    }

    private Project convertToProject(Project project1, ProjectDTO project) {
        project1.setTitle(project.getTitle());
        project1.setSummary(project.getSummary());
        project1.setTechnologies(project.getTechnologies());
        project1.setGithubUrl(project.getGithubUrl());
        project1.setFullDescription(project.getFullDescription());
        project1.setSecurityFocus(project.getSecurityFocus());
        project1.setDemoUrl(project.getDemoUrl());
        project1.setImageUrl(project.getImageUrl());
        project1.setFeatured(project.isFeatured());
        return projectRepository.save(project1);
    }
}
