package org.tibocv.apicv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.tibocv.apicv.model.Education;

public interface EducationRepository extends JpaRepository<Education, Long> {
}
