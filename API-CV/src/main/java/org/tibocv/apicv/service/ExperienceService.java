package org.tibocv.apicv.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.tibocv.apicv.dto.ExperienceDTO;
import org.tibocv.apicv.model.Experience;
import org.tibocv.apicv.repository.ExperienceRepository;

@Service
public class ExperienceService {

    private final ExperienceRepository experienceRepository;

    public ExperienceService(ExperienceRepository experienceRepository) {
        this.experienceRepository = experienceRepository;
    }

    public Experience addExperience(@Valid ExperienceDTO experience) {
        return convertToExperience(new Experience(), experience);
    }

    public void deleteExperience(Long id){experienceRepository.deleteById(id);}

    public void updateExperience(long id, @Valid ExperienceDTO experience){
        Experience experience1 = experienceRepository.findById(id).orElse(null);
        if (experience1 == null) return;
        convertToExperience(experience1, experience);
    }

    private Experience convertToExperience(Experience experience1, ExperienceDTO experience) {
        experience1.setCompanyName(experience.getCompanyName());
        experience1.setPosition(experience.getPosition());
        experience1.setLocation(experience.getLocation());
        experience1.setStartDate(experience.getStartDate());
        experience1.setDescription(experience.getDescription());
        experience1.setToolsUsed(experience.getToolsUsed());
        experience1.setEndDate(experience.getEndDate());
        return experienceRepository.save(experience1);
    }


}
