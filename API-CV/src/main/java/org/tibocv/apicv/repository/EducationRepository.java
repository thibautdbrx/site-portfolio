package org.tibocv.apicv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tibocv.apicv.model.Education;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
}
