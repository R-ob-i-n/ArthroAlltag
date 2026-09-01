package Therapie_Management.backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Basis-Entity für Patient und Therapeut.
 *
 * Die ID (z.B. "v110001") wird nicht automatisch generiert, sondern beim Anlegen fest
 * vergeben - das Präfix (v11 = Patient, v22 = Therapeut) ist reine Lesbarkeits-Konvention.
 * Massgeblich fuer die Rolle ist ausschliesslich das eigene `role`-Feld unten, das beim
 * Anlegen serverseitig gesetzt wird (Patient/Therapeut-Konstruktoren) - niemals eine vom
 * Frontend mitgeschickte Angabe.
 */
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public abstract class User {

    @Id
    @Column(length = 10)
    private String id;

    @Column(nullable = false)
    private String vorname;

    @Column(nullable = false)
    private String nachname;

    private LocalDate geburtsdatum;

    @Column(name = "passwort_hash", nullable = false)
    private String passwortHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @CreationTimestamp
    @Column(name = "erstellt_am", updatable = false)
    private LocalDateTime erstelltAm;

    @UpdateTimestamp
    @Column(name = "aktualisiert_am")
    private LocalDateTime aktualisiertAm;

    protected User(String id, String vorname, String nachname, LocalDate geburtsdatum,
                    String passwortHash, Role role) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.passwortHash = passwortHash;
        this.role = role;
    }
}
