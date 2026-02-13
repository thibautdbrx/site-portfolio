package org.tibocv.apicv.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.tibocv.apicv.dto.EducationDTO;
import org.tibocv.apicv.model.Education;
import org.tibocv.apicv.repository.EducationRepository;

@Service
public class EducationService {

    private final EducationRepository educationRepository;

    public EducationService(EducationRepository educationRepository) {
        this.educationRepository = educationRepository;
    }

    public Education addEducation(@Valid EducationDTO education) {
        return convertToEducation(new Education(), education);
    }

    public void deleteEducation(Long id){educationRepository.deleteById(id);}

    public void updateEducation(long id, @Valid EducationDTO education){
        Education education1 = educationRepository.findById(id).orElse(null);
        if (education1 == null) return;
        convertToEducation(education1, education);

    }

    private Education convertToEducation(Education education1, EducationDTO education){
        education1.setSchoolName(education.getSchoolName());
        education1.setDegree(education.getDegree());
        education1.setFieldOfStudy(education.getFieldOfStudy());
        education1.setStartDate(education.getStartDate());
        education1.setEndDate(education.getEndDate());
        education1.setRelevantCourses(education.getRelevantCourses());
        education1.setDescription(education.getDescription());
        education1.setLocation(education.getLocation());
        education1.setLogo(education.getLogo());
        education1.setUrl(education.getUrl());
        return educationRepository.save(education1);
    }
}
