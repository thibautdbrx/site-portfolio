package org.tibocv.apicv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tibocv.apicv.model.Experience;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
}
