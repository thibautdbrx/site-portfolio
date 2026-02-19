package org.tibocv.apicv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tibocv.apicv.model.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
}
