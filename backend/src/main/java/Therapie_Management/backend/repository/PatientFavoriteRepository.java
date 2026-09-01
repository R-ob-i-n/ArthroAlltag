package Therapie_Management.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Therapie_Management.backend.entity.PatientFavorite;

public interface PatientFavoriteRepository extends JpaRepository<PatientFavorite, Integer> {

    List<PatientFavorite> findByPatientId(String patientId);

    boolean existsByPatientIdAndTippId(String patientId, Integer tippId);
}
