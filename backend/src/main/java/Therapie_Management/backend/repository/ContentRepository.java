package Therapie_Management.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Therapie_Management.backend.entity.Content;

public interface ContentRepository extends JpaRepository<Content, Integer> {

    List<Content> findByKategorieId(Integer kategorieId);
}
