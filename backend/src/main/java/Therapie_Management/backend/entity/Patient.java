package Therapie_Management.backend.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "patients")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
@NoArgsConstructor
public class Patient extends User {

    private String diagnostik;

    private String notizen;

    public Patient(String id, String vorname, String nachname, LocalDate geburtsdatum,
                   String passwortHash, String diagnostik, String notizen) {
        super(id, vorname, nachname, geburtsdatum, passwortHash, Role.PATIENT);
        this.diagnostik = diagnostik;
        this.notizen = notizen;
    }
}
