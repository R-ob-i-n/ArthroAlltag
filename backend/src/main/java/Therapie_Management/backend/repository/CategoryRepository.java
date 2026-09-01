package Therapie_Management.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Therapie_Management.backend.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
