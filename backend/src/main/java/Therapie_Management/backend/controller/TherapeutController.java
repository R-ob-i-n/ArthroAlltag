package Therapie_Management.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Therapie_Management.backend.dto.ContentCreateRequest;
import Therapie_Management.backend.dto.ContentResponse;
import Therapie_Management.backend.dto.ModuleResponse;
import Therapie_Management.backend.dto.PatientResponse;
import Therapie_Management.backend.dto.UnlockModuleRequest;
import Therapie_Management.backend.service.ContentService;
import Therapie_Management.backend.service.ModuleService;
import Therapie_Management.backend.service.PatientService;
import jakarta.validation.Valid;

/**
 * @PreAuthorize stellt sicher, dass nur eingeloggte Therapeuten diese Endpoints nutzen und
 * dass die therapeutId im Pfad zur eigenen User-ID passt - so kann sich niemand als ein
 * anderer Therapeut ausgeben, auch wenn er dessen ID kennt.
 */
@RestController
@RequestMapping("/api/therapeuts/{therapeutId}")
@PreAuthorize("hasRole('THERAPEUT') and #therapeutId == authentication.name")
public class TherapeutController {

    private final ContentService contentService;
    private final ModuleService moduleService;
    private final PatientService patientService;

    public TherapeutController(ContentService contentService, ModuleService moduleService,
                                PatientService patientService) {
        this.contentService = contentService;
        this.moduleService = moduleService;
        this.patientService = patientService;
    }

    /** Patienten-Auswahl fuer die Modul-Freischaltung im Frontend. */
    @GetMapping("/patients")
    public ResponseEntity<List<PatientResponse>> getPatients(@P("therapeutId") @PathVariable String therapeutId) {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    /**
     * Nutzt dieselbe Service-Methode wie PatientController.getUnlockedModules() - dort fuer
     * den Patienten selbst, hier fuer den Therapeuten, der den Freischaltstatus vor dem
     * Freischalten sehen muss.
     */
    @GetMapping("/patients/{patientId}/modules")
    public ResponseEntity<List<ModuleResponse>> getPatientModules(
            @P("therapeutId") @PathVariable String therapeutId,
            @PathVariable String patientId) {
        return ResponseEntity.ok(moduleService.getUnlockedModules(patientId));
    }

    @PostMapping("/contents")
    public ResponseEntity<ContentResponse> createContent(
            @P("therapeutId") @PathVariable String therapeutId,
            @Valid @RequestBody ContentCreateRequest request) {
        ContentResponse content = contentService.createContent(therapeutId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(content);
    }

    @PutMapping("/contents/{contentId}")
    public ResponseEntity<ContentResponse> updateContent(
            @P("therapeutId") @PathVariable String therapeutId,
            @PathVariable Integer contentId,
            @Valid @RequestBody ContentCreateRequest request) {
        return ResponseEntity.ok(contentService.updateContent(therapeutId, contentId, request));
    }

    @DeleteMapping("/contents/{contentId}")
    public ResponseEntity<Void> deleteContent(
            @P("therapeutId") @PathVariable String therapeutId,
            @PathVariable Integer contentId) {
        contentService.deleteContent(therapeutId, contentId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/unlock-module")
    public ResponseEntity<Void> unlockModule(
            @P("therapeutId") @PathVariable String therapeutId,
            @Valid @RequestBody UnlockModuleRequest request) {
        moduleService.unlockModule(therapeutId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /** Nimmt eine Modul-Freischaltung wieder zurueck, z.B. wenn sie versehentlich erteilt wurde. */
    @DeleteMapping("/patients/{patientId}/modules/{modulId}")
    public ResponseEntity<Void> revokeModule(
            @P("therapeutId") @PathVariable String therapeutId,
            @PathVariable String patientId,
            @PathVariable Integer modulId) {
        moduleService.revokeModule(therapeutId, patientId, modulId);
        return ResponseEntity.noContent().build();
    }
}
