package Therapie_Management.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Die vier Therapiemodule sind fachlich fest vorgegeben (Grundlagen, Gelenkschutz,
 * Übungen, Fortgeschrittene). Die IDs werden deshalb bewusst nicht automatisch generiert,
 * sondern per data.sql fest vergeben (1-4).
 */
@Entity
@Table(name = "modules")
@Getter
@Setter
@NoArgsConstructor
public class Module {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer reihenfolge;

    private String beschreibung;

    public Module(Integer id, String name, Integer reihenfolge, String beschreibung) {
        this.id = id;
        this.name = name;
        this.reihenfolge = reihenfolge;
        this.beschreibung = beschreibung;
    }
}
