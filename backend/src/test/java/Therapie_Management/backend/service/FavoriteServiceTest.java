package Therapie_Management.backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import Therapie_Management.backend.dto.FavoriteCreateRequest;
import Therapie_Management.backend.dto.FavoriteResponse;
import Therapie_Management.backend.entity.Content;
import Therapie_Management.backend.entity.Patient;
import Therapie_Management.backend.entity.PatientFavorite;
import Therapie_Management.backend.exception.ForbiddenException;
import Therapie_Management.backend.exception.ValidationException;
import Therapie_Management.backend.repository.ContentRepository;
import Therapie_Management.backend.repository.PatientFavoriteRepository;
import Therapie_Management.backend.repository.PatientRepository;

/**
 * Unit-Test mit gemockten Repositories statt echter Datenbank - schnell, und prueft nur die
 * fachliche Logik in FavoriteService: die Duplikat-Regel beim Hinzufuegen und die
 * Besitzer-Pruefung beim Entfernen (kann Patient A den Favoriten von Patient B loeschen,
 * wenn er zufaellig dessen Favoriten-Id kennt? -> nein).
 */
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ContentRepository contentRepository;

    @Mock
    private PatientFavoriteRepository patientFavoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    @Test
    void addFavorite_speichertNeuenFavoriten_wennNochNichtVorhanden() {
        Patient patient = new Patient("v110001", "Elena", "Fischer", null, "hash", "Rhizarthrose", null);
        Content tipp = new Content();
        tipp.setId(1);

        when(patientRepository.findById("v110001")).thenReturn(Optional.of(patient));
        when(contentRepository.findById(1)).thenReturn(Optional.of(tipp));
        when(patientFavoriteRepository.existsByPatientIdAndTippId("v110001", 1)).thenReturn(false);
        when(patientFavoriteRepository.save(any(PatientFavorite.class))).thenAnswer(invocation -> {
            PatientFavorite favorite = invocation.getArgument(0);
            favorite.setId(42);
            favorite.setHinzugefuegtAm(LocalDateTime.now());
            return favorite;
        });

        FavoriteResponse response = favoriteService.addFavorite("v110001", new FavoriteCreateRequest(1));

        assertThat(response.id()).isEqualTo(42);
        assertThat(response.tippId()).isEqualTo(1);
    }

    @Test
    void addFavorite_wirftValidationException_wennBereitsFavorisiert() {
        when(patientRepository.findById("v110001"))
            .thenReturn(Optional.of(new Patient("v110001", "Elena", "Fischer", null, "hash", "Rhizarthrose", null)));
        when(contentRepository.findById(1)).thenReturn(Optional.of(new Content()));
        when(patientFavoriteRepository.existsByPatientIdAndTippId("v110001", 1)).thenReturn(true);

        assertThatThrownBy(() -> favoriteService.addFavorite("v110001", new FavoriteCreateRequest(1)))
            .isInstanceOf(ValidationException.class)
            .hasMessageContaining("bereits favorisiert");

        verify(patientFavoriteRepository, never()).save(any());
    }

    @Test
    void removeFavorite_wirftForbiddenException_wennFavoritEinemAnderenPatientenGehoert() {
        Patient fremderPatient = new Patient("v110002", "Maria", "Weber", null, "hash", "Rhizarthrose", null);
        PatientFavorite favorite = new PatientFavorite(fremderPatient, new Content());
        favorite.setId(1);

        when(patientFavoriteRepository.findById(1)).thenReturn(Optional.of(favorite));

        assertThatThrownBy(() -> favoriteService.removeFavorite("v110001", 1))
            .isInstanceOf(ForbiddenException.class);

        verify(patientFavoriteRepository, never()).delete(any());
    }
}
