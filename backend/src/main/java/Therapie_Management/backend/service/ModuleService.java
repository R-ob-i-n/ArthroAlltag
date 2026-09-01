package Therapie_Management.backend.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Therapie_Management.backend.dto.ModuleResponse;
import Therapie_Management.backend.dto.UnlockModuleRequest;
import Therapie_Management.backend.entity.Module;
import Therapie_Management.backend.entity.Patient;
import Therapie_Management.backend.entity.PatientModuleAccess;
import Therapie_Management.backend.entity.Therapeut;
import Therapie_Management.backend.exception.ResourceNotFoundException;
import Therapie_Management.backend.exception.ValidationException;
import Therapie_Management.backend.repository.ModuleRepository;
import Therapie_Management.backend.repository.PatientModuleAccessRepository;
import Therapie_Management.backend.repository.PatientRepository;
import Therapie_Management.backend.repository.TherapeutRepository;

@Service
@Transactional
public class ModuleService {

    private final ModuleRepository moduleRepository;
    private final PatientRepository patientRepository;
    private final TherapeutRepository therapeutRepository;
    private final PatientModuleAccessRepository patientModuleAccessRepository;

    public ModuleService(ModuleRepository moduleRepository, PatientRepository patientRepository,
                          TherapeutRepository therapeutRepository,
                          PatientModuleAccessRepository patientModuleAccessRepository) {
        this.moduleRepository = moduleRepository;
        this.patientRepository = patientRepository;
        this.therapeutRepository = therapeutRepository;
        this.patientModuleAccessRepository = patientModuleAccessRepository;
    }

    /** Fuer GET /api/modules - alle 4 fest vorgegebenen Module, unabhaengig vom Freischaltstatus. */
    public List<ModuleResponse> getAllModules() {
        return moduleRepository.findAllByOrderByReihenfolgeAsc().stream()
            .map(this::toResponse)
            .toList();
    }

    public List<ModuleResponse> getUnlockedModules(String patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient nicht gefunden: " + patientId);
        }

        return patientModuleAccessRepository.findByPatientId(patientId).stream()
            .map(PatientModuleAccess::getModul)
            .sorted(Comparator.comparing(Module::getReihenfolge))
            .map(this::toResponse)
            .toList();
    }

    public void unlockModule(String therapeutId, UnlockModuleRequest request) {
        Therapeut therapeut = therapeutRepository.findById(therapeutId)
            .orElseThrow(() -> new ResourceNotFoundException("Therapeut nicht gefunden: " + therapeutId));
        Patient patient = patientRepository.findById(request.patientId())
            .orElseThrow(() -> new ResourceNotFoundException("Patient nicht gefunden: " + request.patientId()));
        Module modul = moduleRepository.findById(request.modulId())
            .orElseThrow(() -> new ResourceNotFoundException("Modul nicht gefunden: " + request.modulId()));

        if (patientModuleAccessRepository.existsByPatientIdAndModulId(request.patientId(), request.modulId())) {
            throw new ValidationException("Modul ist fuer diesen Patienten bereits freigeschaltet");
        }

        patientModuleAccessRepository.save(new PatientModuleAccess(patient, modul, therapeut));
    }

    private ModuleResponse toResponse(Module modul) {
        return new ModuleResponse(modul.getId(), modul.getName(), modul.getReihenfolge(), modul.getBeschreibung());
    }
}
