package it.supermercato.magazzino.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.supermercato.magazzino.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
        // method to find a role by its name, returning an Optional to handle the case where it might not exist
        Optional<Role> findByName(String name);
    
        // method to check if a role with a given name already exists in the database
        boolean existsByName(String name);
}
