package Therapie_Management.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import Therapie_Management.backend.dto.ContentResponse;
import Therapie_Management.backend.service.ContentService;

/**
 * Allgemeiner, rollenunabhaengiger Lesezugriff auf Tipps - z.B. fuer eine Detailansicht,
 * die von einem Favoriten aus verlinkt wird, ohne den Umweg ueber /api/patients/{id}/contents.
 */
@RestController
@RequestMapping("/api/contents")
public class ContentController {

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping
    public ResponseEntity<List<ContentResponse>> getContents(@RequestParam(required = false) Integer categoryId) {
        return ResponseEntity.ok(contentService.getContents(categoryId));
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<ContentResponse> getContent(@PathVariable Integer contentId) {
        return ResponseEntity.ok(contentService.getContent(contentId));
    }
}
