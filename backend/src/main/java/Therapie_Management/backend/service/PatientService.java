package Therapie_Management.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Therapie_Management.backend.dto.ContentResponse;
import Therapie_Management.backend.dto.FavoriteResponse;
import Therapie_Management.backend.dto.PatientResponse;
import Therapie_Management.backend.entity.Patient;
import Therapie_Management.backend.exception.ResourceNotFoundException;
import Therapie_Management.backend.repository.PatientFavoriteRepository;
import Therapie_Management.backend.repository.PatientRepository;

@Service
@Transactional(readOnly = true)
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientFavoriteRepository patientFavoriteRepository;
    private final ContentService contentService;

    public PatientService(PatientRepository patientRepository, PatientFavoriteRepository patientFavoriteRepository,
                           ContentService contentService) {
        this.patientRepository = patientRepository;
        this.patientFavoriteRepository = patientFavoriteRepository;
        this.contentService = contentService;
    }

    public PatientResponse getPatient(String patientId) {
        return toResponse(findPatientOrThrow(patientId));
    }

    /** Fuer die Therapeuten-Ansicht: Patienten-Auswahl fuer die Modul-Freischaltung. */
    public List<PatientResponse> getAllPatients() {
        return patientRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<ContentResponse> getContentsByCategory(String patientId, Integer kategorieId) {
        findPatientOrThrow(patientId);
        return contentService.getContents(kategorieId);
    }

    public List<FavoriteResponse> getFavorites(String patientId) {
        findPatientOrThrow(patientId);
        return patientFavoriteRepository.findByPatientId(patientId).stream()
            .map(favorite -> new FavoriteResponse(favorite.getId(), favorite.getTipp().getId(), favorite.getHinzugefuegtAm()))
            .toList();
    }

    private Patient findPatientOrThrow(String patientId) {
        return patientRepository.findById(patientId)
            .orElseThrow(() -> new ResourceNotFoundException("Patient nicht gefunden: " + patientId));
    }

    private PatientResponse toResponse(Patient patient) {
        return new PatientResponse(patient.getId(), patient.getVorname(), patient.getNachname(), patient.getDiagnostik());
    }
}
