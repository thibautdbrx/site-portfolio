package org.tibocv.apicv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tibocv.apicv.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{
}
