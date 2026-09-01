package Therapie_Management.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Therapie_Management.backend.dto.FavoriteCreateRequest;
import Therapie_Management.backend.dto.FavoriteResponse;
import Therapie_Management.backend.entity.Content;
import Therapie_Management.backend.entity.Patient;
import Therapie_Management.backend.entity.PatientFavorite;
import Therapie_Management.backend.exception.ForbiddenException;
import Therapie_Management.backend.exception.ResourceNotFoundException;
import Therapie_Management.backend.exception.ValidationException;
import Therapie_Management.backend.repository.ContentRepository;
import Therapie_Management.backend.repository.PatientFavoriteRepository;
import Therapie_Management.backend.repository.PatientRepository;

@Service
@Transactional
public class FavoriteService {

    private final PatientRepository patientRepository;
    private final ContentRepository contentRepository;
    private final PatientFavoriteRepository patientFavoriteRepository;

    public FavoriteService(PatientRepository patientRepository, ContentRepository contentRepository,
                            PatientFavoriteRepository patientFavoriteRepository) {
        this.patientRepository = patientRepository;
        this.contentRepository = contentRepository;
        this.patientFavoriteRepository = patientFavoriteRepository;
    }

    public FavoriteResponse addFavorite(String patientId, FavoriteCreateRequest request) {
        Patient patient = patientRepository.findById(patientId)
            .orElseThrow(() -> new ResourceNotFoundException("Patient nicht gefunden: " + patientId));
        Content tipp = contentRepository.findById(request.tippId())
            .orElseThrow(() -> new ResourceNotFoundException("Tipp nicht gefunden: " + request.tippId()));

        if (patientFavoriteRepository.existsByPatientIdAndTippId(patientId, request.tippId())) {
            throw new ValidationException("Tipp ist bereits favorisiert");
        }

        PatientFavorite saved = patientFavoriteRepository.save(new PatientFavorite(patient, tipp));
        return new FavoriteResponse(saved.getId(), saved.getTipp().getId(), saved.getHinzugefuegtAm());
    }

    public void removeFavorite(String patientId, Integer favoriteId) {
        PatientFavorite favorite = patientFavoriteRepository.findById(favoriteId)
            .orElseThrow(() -> new ResourceNotFoundException("Favorit nicht gefunden: " + favoriteId));

        if (!favorite.getPatient().getId().equals(patientId)) {
            throw new ForbiddenException("Favorit gehoert nicht zu diesem Patienten");
        }

        patientFavoriteRepository.delete(favorite);
    }
}
