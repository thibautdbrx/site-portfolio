package org.tibocv.apicv.dto;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceDTO {
    private String companyName;
    private String position;
    private String location;
    private String startDate;
    private String endDate;
    private String description;
    private List<String> toolsUsed;
}
