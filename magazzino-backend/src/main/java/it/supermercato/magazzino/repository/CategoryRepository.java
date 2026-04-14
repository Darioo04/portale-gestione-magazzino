package it.supermercato.magazzino.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.supermercato.magazzino.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    
    // method to find a category by its name, returning an Optional to handle the case where it might not exist
    Optional<Category> findByName(String name);

    // method to check if a category with a given name already exists in the database
    boolean existsByName(String name);
}
