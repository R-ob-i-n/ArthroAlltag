package Therapie_Management.backend.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "therapeuten")
@PrimaryKeyJoinColumn(name = "user_id")
@Getter
@Setter
@NoArgsConstructor
public class Therapeut extends User {

    private String email;

    private String spezialisierung;

    public Therapeut(String id, String vorname, String nachname, LocalDate geburtsdatum,
                      String passwortHash, String email, String spezialisierung) {
        super(id, vorname, nachname, geburtsdatum, passwortHash, Role.THERAPEUT);
        this.email = email;
        this.spezialisierung = spezialisierung;
    }
}
