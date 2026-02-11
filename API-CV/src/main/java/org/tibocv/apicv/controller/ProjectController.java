package org.tibocv.apicv.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.tibocv.apicv.dto.ProjectDTO;
import org.tibocv.apicv.model.Project;
import org.tibocv.apicv.service.ProjectService;

@RestController
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public Project addProject(@RequestBody @Valid ProjectDTO project ){ return projectService.addProject(project);}

    @DeleteMapping
    public void deleteProject(@RequestParam Long id){projectService.deleteProject(id);}

    @PutMapping("/{id}")
    public void updateProject(@PathVariable Long id, @RequestBody @Valid ProjectDTO project){projectService.updateProject(id, project);}
}
