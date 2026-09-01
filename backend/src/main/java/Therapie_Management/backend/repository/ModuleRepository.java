package Therapie_Management.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Therapie_Management.backend.entity.Module;

public interface ModuleRepository extends JpaRepository<Module, Integer> {

    List<Module> findAllByOrderByReihenfolgeAsc();
}
