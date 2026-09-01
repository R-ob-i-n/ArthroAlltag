package Therapie_Management.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import Therapie_Management.backend.dto.CategoryResponse;
import Therapie_Management.backend.dto.ContentCreateRequest;
import Therapie_Management.backend.dto.ContentResponse;
import Therapie_Management.backend.entity.Category;
import Therapie_Management.backend.entity.Content;
import Therapie_Management.backend.entity.Therapeut;
import Therapie_Management.backend.exception.ResourceNotFoundException;
import Therapie_Management.backend.repository.CategoryRepository;
import Therapie_Management.backend.repository.ContentRepository;
import Therapie_Management.backend.repository.TherapeutRepository;

@Service
@Transactional
public class ContentService {

    private final ContentRepository contentRepository;
    private final CategoryRepository categoryRepository;
    private final TherapeutRepository therapeutRepository;

    public ContentService(ContentRepository contentRepository, CategoryRepository categoryRepository,
                           TherapeutRepository therapeutRepository) {
        this.contentRepository = contentRepository;
        this.categoryRepository = categoryRepository;
        this.therapeutRepository = therapeutRepository;
    }

    /**
     * Wird auch von PatientService.getContentsByCategory() genutzt: Tipps sind fuer alle
     * Patienten gleichermassen lesbar, die Filterung/Mapping soll deshalb nur an einer
     * Stelle existieren statt in PatientService dupliziert zu werden.
     */
    public List<ContentResponse> getContents(Integer kategorieId) {
        List<Content> contents = kategorieId != null
            ? contentRepository.findByKategorieId(kategorieId)
            : contentRepository.findAll();

        return contents.stream().map(this::toResponse).toList();
    }

    public ContentResponse getContent(Integer contentId) {
        return toResponse(findContentOrThrow(contentId));
    }

    public ContentResponse createContent(String therapeutId, ContentCreateRequest request) {
        Therapeut therapeut = findTherapeutOrThrow(therapeutId);
        Category kategorie = findCategoryOrThrow(request.kategorieId());

        Content content = new Content();
        applyRequest(content, request, kategorie);
        content.setErstelltDurch(therapeut);

        return toResponse(contentRepository.save(content));
    }

    public ContentResponse updateContent(String therapeutId, Integer contentId, ContentCreateRequest request) {
        findTherapeutOrThrow(therapeutId);
        Content content = findContentOrThrow(contentId);
        Category kategorie = findCategoryOrThrow(request.kategorieId());

        applyRequest(content, request, kategorie);

        return toResponse(contentRepository.save(content));
    }

    public void deleteContent(String therapeutId, Integer contentId) {
        findTherapeutOrThrow(therapeutId);
        Content content = findContentOrThrow(contentId);
        contentRepository.delete(content);
    }

    private void applyRequest(Content content, ContentCreateRequest request, Category kategorie) {
        content.setTitel(request.titel());
        content.setBeschreibung(request.beschreibung());
        content.setKategorie(kategorie);
        content.setBildUrl(request.bildUrl());
        content.setSchwierigkeitsgrad(request.schwierigkeitsgrad());
        content.setHilfsmittel(request.hilfsmittel());
    }

    private Therapeut findTherapeutOrThrow(String therapeutId) {
        return therapeutRepository.findById(therapeutId)
            .orElseThrow(() -> new ResourceNotFoundException("Therapeut nicht gefunden: " + therapeutId));
    }

    private Content findContentOrThrow(Integer contentId) {
        return contentRepository.findById(contentId)
            .orElseThrow(() -> new ResourceNotFoundException("Tipp nicht gefunden: " + contentId));
    }

    private Category findCategoryOrThrow(Integer kategorieId) {
        return categoryRepository.findById(kategorieId)
            .orElseThrow(() -> new ResourceNotFoundException("Kategorie nicht gefunden: " + kategorieId));
    }

    /**
     * kategorie/erstelltDurch sind LAZY (siehe Content-Entity) - jeder Aufruf hier loest bei
     * einer Liste von Tipps klassisches N+1 aus. Bei der Datenmenge dieses Projekts (siehe
     * data.sql) faellt das nicht ins Gewicht; bei echten Datenmengen wuerde man stattdessen
     * z.B. @EntityGraph oder eine JOIN-FETCH-Query verwenden.
     */
    private ContentResponse toResponse(Content content) {
        CategoryResponse kategorie = new CategoryResponse(content.getKategorie().getId(), content.getKategorie().getName());
        String erstelltDurchName = content.getErstelltDurch().getVorname() + " " + content.getErstelltDurch().getNachname();

        return new ContentResponse(
            content.getId(),
            content.getTitel(),
            content.getBeschreibung(),
            kategorie,
            content.getBildUrl(),
            content.getSchwierigkeitsgrad(),
            content.getHilfsmittel(),
            erstelltDurchName,
            content.getErstelltAm(),
            content.getAktualisiertAm()
        );
    }
}
