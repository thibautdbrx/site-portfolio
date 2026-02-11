package org.tibocv.apicv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tibocv.apicv.dto.ProfileDTO;
import org.tibocv.apicv.model.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

}
