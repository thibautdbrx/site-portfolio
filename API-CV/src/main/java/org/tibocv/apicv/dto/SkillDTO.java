package org.tibocv.apicv.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SkillDTO {
    private Long id;
    private String name;
    private String category;
    private String level;
    private String iconUrl;
}
