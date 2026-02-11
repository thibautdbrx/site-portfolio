package org.tibocv.apicv.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.tibocv.apicv.dto.SkillDTO;
import org.tibocv.apicv.model.Skill;
import org.tibocv.apicv.service.SkillService;

@RestController
@RequestMapping("/api/skill")
public class SkillController {

    private final SkillService skillService;
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    public Skill addSkill(@RequestBody @Valid SkillDTO skill) { return skillService.addSkill(skill);}

    @DeleteMapping
    public void deleteSkill(@RequestParam Long id){skillService.deleteSkill(id);}

    @PutMapping("/{id}")
    public void updateSkill(@PathVariable Long id, @RequestBody @Valid SkillDTO skill){skillService.updateSkill(id, skill);}
}
