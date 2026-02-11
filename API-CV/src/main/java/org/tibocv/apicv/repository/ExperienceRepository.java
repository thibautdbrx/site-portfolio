package org.tibocv.apicv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tibocv.apicv.model.Experience;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
}
