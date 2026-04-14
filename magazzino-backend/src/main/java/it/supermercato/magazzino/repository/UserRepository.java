package it.supermercato.magazzino.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.supermercato.magazzino.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    
    Optional<User> findByUsername(String username);

    Optional<User> findByRole(String role);

    boolean existsByUsername(String username);

    boolean existsByPersonId(Integer personId);
}
