package org.tibocv.apicv.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation;

    // Liste blanche des extensions autorisées (Cyber-secu basic)
    private final List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif", "pdf", "svg");

    // On injecte le chemin depuis application.properties
    public FileStorageService(@Value("${app.upload.dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir);
    }

    @PostConstruct
    public void init() {
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
        } catch (IOException e) {
            throw new RuntimeException("Impossible d'initialiser le dossier de stockage !", e);
        }
    }

    public String save(MultipartFile file) {
        // 1. Vérification que le fichier n'est pas vide
        if (file.isEmpty()) {
            throw new RuntimeException("Erreur : Fichier vide.");
        }

        // 2. Nettoyage et Validation de l'extension
        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = getExtension(originalFilename);

        if (!allowedExtensions.contains(extension.toLowerCase())) {
            throw new RuntimeException("Type de fichier non autorisé ! (Seulement images et PDF)");
        }

        // 3. Renommage sécurisé (UUID) pour éviter les collisions et noms bizarres
        String newFilename = UUID.randomUUID().toString() + "." + extension;

        try {
            // 4. Copie du fichier (écrase si existe déjà, mais improbable avec UUID)
            Files.copy(file.getInputStream(), this.rootLocation.resolve(newFilename), StandardCopyOption.REPLACE_EXISTING);
            return newFilename;
        } catch (IOException e) {
            throw new RuntimeException("Impossible de stocker le fichier " + newFilename, e);
        }
    }

    public Resource load(String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Fichier non trouvé ou illisible : " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Erreur : " + e.getMessage());
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return "";
        int lastUnixPos = filename.lastIndexOf('/');
        int lastWindowsPos = filename.lastIndexOf('\\');
        int index = Math.max(lastUnixPos, lastWindowsPos);
        index = Math.max(index, filename.lastIndexOf('.'));
        if (index < 0) return "";
        return filename.substring(index + 1);
    }
}