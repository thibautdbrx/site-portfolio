package org.tibocv.apicv.service;

import org.springframework.stereotype.Service;
import org.tibocv.apicv.dto.ProfileDTO;
import org.tibocv.apicv.model.Profile;
import org.tibocv.apicv.repository.ProfileRepository;

@Service
public class ProfileService {

    public final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Profile getProfil(){
        return profileRepository.findAll().stream().findFirst().orElse(new Profile());
    }

    //public void deleteProfil(){profilRepository.deleteAll();}

    public void updateProfil(ProfileDTO profile){
        Profile profile1 = getProfil();
        profile1.setFirstName(profile.getFirstName());
        profile1.setLastName(profile.getLastName());
        profile1.setTitle(profile.getTitle());
        profile1.setBio(profile.getBio());
        profile1.setLocation(profile.getLocation());
        profile1.setGithubUrl(profile.getGithubUrl());
        profile1.setLinkedinUrl(profile.getLinkedinUrl());
        profile1.setEmail(profile.getEmail());
        profile1.setPhone(profile.getPhone());
        profile1.setCvPdfUrl(profile.getCvPdfUrl());
        profile1.setTryHackMeUrl(profile.getTryHackMeUrl());
        profile1.setFirstLanguage(profile.getFirstLanguage());
        profile1.setSecondLanguage(profile.getSecondLanguage());
        profile1.setAvatarUrl(profile.getAvatarUrl());
        profileRepository.save(profile1);
    }
}
