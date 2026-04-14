package it.supermercato.magazzino.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.supermercato.magazzino.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    boolean existsByEmail(String email);
    
    Optional<Person> findByEmail(String email);

    Optional<Person> findByPhone(String phone);
}
