package org.tibocv.apicv.service;

import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.tibocv.apicv.dto.SkillDTO;
import org.tibocv.apicv.model.Skill;
import org.tibocv.apicv.repository.SkillRepository;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }


    public Skill addSkill(@Valid SkillDTO skill) {
        return convertToSkill(new Skill(), skill);
    }

    public void deleteSkill(Long id){skillRepository.deleteById(id);}

    public void updateSkill(long id, @Valid SkillDTO skill){
        Skill skill1 = skillRepository.findById(id).orElse(null);
        if (skill1 == null) return;
        convertToSkill(skill1, skill);
    }

    private Skill convertToSkill(Skill skill, SkillDTO skillDTO) {
        skill.setName(skillDTO.getName());
        skill.setCategory(skillDTO.getCategory());
        skill.setLevel(skillDTO.getLevel());
        skill.setIconUrl(skillDTO.getIconUrl());
        return skillRepository.save(skill);
    }
}
