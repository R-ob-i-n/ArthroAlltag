package Therapie_Management.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Therapie_Management.backend.entity.PatientModuleAccess;

public interface PatientModuleAccessRepository extends JpaRepository<PatientModuleAccess, Integer> {

    List<PatientModuleAccess> findByPatientId(String patientId);

    boolean existsByPatientIdAndModulId(String patientId, Integer modulId);
}
