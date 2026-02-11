package org.tibocv.apicv.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.tibocv.apicv.dto.ProfileDTO;
import org.tibocv.apicv.model.Profile;
import org.tibocv.apicv.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
public class ProfilController {

    private final ProfileService profileService;

    public ProfilController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public Profile getProfil(){
        return profileService.getProfil();
    }

    @PutMapping
    public void updateProfile(@RequestBody @Valid ProfileDTO profile){ profileService.updateProfil(profile); }
}
