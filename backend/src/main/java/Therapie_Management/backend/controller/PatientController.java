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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Therapie_Management.backend.dto.ContentResponse;
import Therapie_Management.backend.dto.FavoriteCreateRequest;
import Therapie_Management.backend.dto.FavoriteResponse;
import Therapie_Management.backend.dto.ModuleResponse;
import Therapie_Management.backend.dto.PatientResponse;
import Therapie_Management.backend.service.FavoriteService;
import Therapie_Management.backend.service.ModuleService;
import Therapie_Management.backend.service.PatientService;
import jakarta.validation.Valid;

/**
 * Nur lesende Endpoints (bis auf Favoriten setzen/entfernen).
 *
 * @PreAuthorize auf Klassenebene gilt fuer alle Methoden: der Aufrufer muss die Rolle
 * PATIENT haben UND die patientId aus dem Pfad muss exakt der eigenen User-ID entsprechen
 * (authentication.name wird vom JwtAuthenticationFilter aus dem Token gesetzt). Damit kann
 * Patient v110001 z.B. nicht ueber /api/patients/v110002/favorites auf fremde Daten zugreifen.
 */
@RestController
@RequestMapping("/api/patients/{patientId}")
@PreAuthorize("hasRole('PATIENT') and #patientId == authentication.name")
public class PatientController {

    private final PatientService patientService;
    private final FavoriteService favoriteService;
    private final ModuleService moduleService;

    public PatientController(PatientService patientService, FavoriteService favoriteService,
                              ModuleService moduleService) {
        this.patientService = patientService;
        this.favoriteService = favoriteService;
        this.moduleService = moduleService;
    }

    @GetMapping
    public ResponseEntity<PatientResponse> getPatient(@P("patientId") @PathVariable String patientId) {
        return ResponseEntity.ok(patientService.getPatient(patientId));
    }

    @GetMapping("/contents")
    public ResponseEntity<List<ContentResponse>> getContents(
            @P("patientId") @PathVariable String patientId,
            @RequestParam(required = false) Integer categoryId) {
        return ResponseEntity.ok(patientService.getContentsByCategory(patientId, categoryId));
    }

    @GetMapping("/favorites")
    public ResponseEntity<List<FavoriteResponse>> getFavorites(@P("patientId") @PathVariable String patientId) {
        return ResponseEntity.ok(patientService.getFavorites(patientId));
    }

    @PostMapping("/favorites")
    public ResponseEntity<FavoriteResponse> addFavorite(
            @P("patientId") @PathVariable String patientId,
            @Valid @RequestBody FavoriteCreateRequest request) {
        FavoriteResponse favorite = favoriteService.addFavorite(patientId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(favorite);
    }

    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity<Void> removeFavorite(
            @P("patientId") @PathVariable String patientId,
            @PathVariable Integer favoriteId) {
        favoriteService.removeFavorite(patientId, favoriteId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/modules")
    public ResponseEntity<List<ModuleResponse>> getUnlockedModules(@P("patientId") @PathVariable String patientId) {
        return ResponseEntity.ok(moduleService.getUnlockedModules(patientId));
    }
}
