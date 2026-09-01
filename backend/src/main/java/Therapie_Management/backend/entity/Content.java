package Therapie_Management.backend.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entspricht dem "Tipp" aus der Spezifikation (Alltagstipp/Übung).
 * Klassenname "Content", da er sowohl Alltagstipps als auch Übungen abbildet.
 */
@Entity
@Table(name = "contents")
@Getter
@Setter
@NoArgsConstructor
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String titel;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String beschreibung;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kategorie_id", nullable = false)
    private Category kategorie;

    @Column(name = "bild_url")
    private String bildUrl;

    @Enumerated(EnumType.STRING)
    private Schwierigkeitsgrad schwierigkeitsgrad;

    private String hilfsmittel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "erstellt_durch_id", nullable = false)
    private Therapeut erstelltDurch;

    @CreationTimestamp
    @Column(name = "erstellt_am", updatable = false)
    private LocalDateTime erstelltAm;

    @UpdateTimestamp
    @Column(name = "aktualisiert_am")
    private LocalDateTime aktualisiertAm;
}
