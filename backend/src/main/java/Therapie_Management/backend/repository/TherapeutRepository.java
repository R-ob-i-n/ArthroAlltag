package Therapie_Management.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Therapie_Management.backend.entity.Therapeut;

public interface TherapeutRepository extends JpaRepository<Therapeut, String> {
}
