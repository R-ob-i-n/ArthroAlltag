package Therapie_Management.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Therapie_Management.backend.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, String> {
}
