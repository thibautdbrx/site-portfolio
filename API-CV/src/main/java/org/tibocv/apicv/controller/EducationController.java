package org.tibocv.apicv.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.tibocv.apicv.dto.EducationDTO;
import org.tibocv.apicv.model.Education;
import org.tibocv.apicv.service.EducationService;

@RestController
@RequestMapping("/api/education")
public class EducationController {

    private final EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @PostMapping
    public Education addEducation(@RequestBody @Valid EducationDTO education){ return educationService.addEducation(education);}

    @DeleteMapping
    public void deleteEducation(@RequestParam Long id){educationService.deleteEducation(id);}

    @PutMapping("/{id}")
    public void updateEducation(@PathVariable Long id, @RequestBody @Valid EducationDTO education){educationService.updateEducation(id, education);}
}
