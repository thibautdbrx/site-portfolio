package org.tibocv.apicv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tibocv.apicv.model.Skill;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}
