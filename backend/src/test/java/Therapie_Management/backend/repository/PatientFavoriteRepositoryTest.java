package Therapie_Management.backend.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import Therapie_Management.backend.entity.Category;
import Therapie_Management.backend.entity.Content;
import Therapie_Management.backend.entity.Patient;
import Therapie_Management.backend.entity.PatientFavorite;
import Therapie_Management.backend.entity.Therapeut;

/**
 * @DataJpaTest laedt nur die JPA-Schicht (kein Web, keine Security) und legt eigene
 * Testdaten per TestEntityManager an, statt sich auf data.sql zu verlassen - so bleibt der
 * Test unabhaengig von spaeteren Aenderungen an den Fixture-Daten. Geprueft wird die von
 * Spring Data aus dem Methodennamen abgeleitete Query (existsByPatientIdAndTippId).
 */
@DataJpaTest
class PatientFavoriteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PatientFavoriteRepository patientFavoriteRepository;

    @Test
    void existsByPatientIdAndTippId_erkenntVorhandenenFavoritenUndUnterscheidetFremdeTipps() {
        Patient patient = entityManager.persist(
            new Patient("v110099", "Test", "Patientin", LocalDate.of(1990, 1, 1), "hash", "Testdiagnose", null));
        Therapeut therapeut = entityManager.persist(
            new Therapeut("v220099", "Test", "Therapeut", LocalDate.of(1985, 1, 1), "hash", "test@example.com", "Test"));
        Category kategorie = entityManager.persist(new Category("Testkategorie"));

        Content content = new Content();
        content.setTitel("Testtipp");
        content.setBeschreibung("Testbeschreibung");
        content.setKategorie(kategorie);
        content.setErstelltDurch(therapeut);
        content = entityManager.persist(content);

        entityManager.persist(new PatientFavorite(patient, content));
        entityManager.flush();

        assertThat(patientFavoriteRepository.existsByPatientIdAndTippId("v110099", content.getId())).isTrue();
        assertThat(patientFavoriteRepository.existsByPatientIdAndTippId("v110099", content.getId() + 1)).isFalse();
    }
}
