package org.tibocv.apicv.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.tibocv.apicv.dto.ExperienceDTO;
import org.tibocv.apicv.model.Experience;
import org.tibocv.apicv.service.ExperienceService;

@RestController
@RequestMapping("/api/experience")
public class ExperienceController {

    private final ExperienceService experienceService;

    public ExperienceController(ExperienceService experienceService) {
        this.experienceService = experienceService;
    }

    @PostMapping
    public Experience addExperience(@RequestBody @Valid ExperienceDTO experience) {
        return experienceService.addExperience(experience);
    }

    @DeleteMapping
    public void deleteExperience(@RequestParam Long id) { experienceService.deleteExperience(id); }

    @PutMapping("/{id}")
    public void updateExperience(@PathVariable Long id, @RequestBody @Valid ExperienceDTO experience) { experienceService.updateExperience(id, experience); }
}
