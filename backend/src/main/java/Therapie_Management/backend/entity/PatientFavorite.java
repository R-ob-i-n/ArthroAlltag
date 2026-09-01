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

@Entity
@Table(
    name = "patient_favorites",
    uniqueConstraints = @UniqueConstraint(columnNames = {"patient_id", "tipp_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class PatientFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipp_id", nullable = false)
    private Content tipp;

    @CreationTimestamp
    @Column(name = "hinzugefuegt_am", updatable = false)
    private LocalDateTime hinzugefuegtAm;

    public PatientFavorite(Patient patient, Content tipp) {
        this.patient = patient;
        this.tipp = tipp;
    }
}
