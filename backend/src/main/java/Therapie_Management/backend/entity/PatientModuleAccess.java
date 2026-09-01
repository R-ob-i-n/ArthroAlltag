package Therapie_Management.backend.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Bildet die Freischaltung eines Moduls für einen Patienten ab (klassische N:M-Beziehung
 * mit Zusatzinformationen wie Zeitpunkt und ausführendem Therapeuten, daher als eigene
 * Entity statt als reine @ManyToMany-Verknüpfung modelliert).
 */
@Entity
@Table(
    name = "patient_modul_zugang",
    uniqueConstraints = @UniqueConstraint(columnNames = {"patient_id", "modul_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class PatientModuleAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modul_id", nullable = false)
    private Module modul;

    @CreationTimestamp
    @Column(name = "freigeschaltet_am", updatable = false)
    private LocalDateTime freigeschaltetAm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freigeschaltet_von", nullable = false)
    private Therapeut freigeschaltetVon;

    public PatientModuleAccess(Patient patient, Module modul, Therapeut freigeschaltetVon) {
        this.patient = patient;
        this.modul = modul;
        this.freigeschaltetVon = freigeschaltetVon;
    }
}
