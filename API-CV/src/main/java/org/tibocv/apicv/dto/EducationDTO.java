package org.tibocv.apicv.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class EducationDTO {
    private String schoolName;
    private String degree;
    private String fieldOfStudy;
    private String startDate;
    private String endDate;
    private List<String> relevantCourses;
    private String logo;
    private String url;
    private String description;
    private String location;
}
