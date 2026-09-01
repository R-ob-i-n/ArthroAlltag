package Therapie_Management.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Therapie_Management.backend.entity.User;

/**
 * Login braucht Zugriff auf User (unabhängig davon, ob Patient oder Therapeut),
 * um passwortHash und role zu prüfen, bevor die Rolle feststeht.
 */
public interface UserRepository extends JpaRepository<User, String> {
}
