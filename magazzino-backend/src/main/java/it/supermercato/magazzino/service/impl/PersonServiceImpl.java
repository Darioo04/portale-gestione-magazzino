package it.supermercato.magazzino.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import it.supermercato.magazzino.dto.PersonDTO;
import it.supermercato.magazzino.service.PersonService;
import it.supermercato.magazzino.entity.Person;
import it.supermercato.magazzino.repository.PersonRepository;


@Service
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public PersonDTO createPerson(PersonDTO personDTO) {
        if (personDTO == null) {
            throw new IllegalArgumentException("PersonDTO cannot be null");
        } else if (personDTO.getFirstName() == null || personDTO.getFirstName().isBlank()) {
            throw new IllegalArgumentException("Person first name cannot be null or blank");
        } else if (personDTO.getLastName() == null || personDTO.getLastName().isBlank()) {
            throw new IllegalArgumentException("Person last name cannot be null or blank");
        } else if (personDTO.getEmail() == null || personDTO.getEmail().isBlank()) {
            throw new IllegalArgumentException("Person email cannot be null or blank");
        } else if (personRepository.existsByEmail(personDTO.getEmail())) {
            throw new IllegalArgumentException("A person with the email '" + personDTO.getEmail() + "' already exists.");
        } else {
            var newEntity = new Person(personDTO.getFirstName(), personDTO.getLastName(), personDTO.getEmail(), personDTO.getPhone());
            var savedEntity = personRepository.save(newEntity);
            return new PersonDTO(savedEntity.getId(), savedEntity.getFirstName(), savedEntity.getLastName(), savedEntity.getEmail(), savedEntity.getPhone());
        }
    }

    @Override
    public void deletePerson(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Person ID cannot be null");
        } else if (!personRepository.existsById(id)) {
            throw new IllegalArgumentException("No person found with ID: " + id);
        } else {
            personRepository.deleteById(id);
        }
        
    }

    @Override
    public List<PersonDTO> getAllPersons() {
        List<Person> persons = personRepository.findAll();
        return persons.stream()
            .map(person -> new PersonDTO(person.getId(), person.getFirstName(), person.getLastName(), person.getEmail(), person.getPhone()))
            .collect(Collectors.toList());

    }

    @Override
    public PersonDTO getPersonById(Integer id) {
            if (id == null) {
                throw new IllegalArgumentException("Person ID cannot be null");
            } else {
                return personRepository.findById(id)
                    .map(person -> new PersonDTO(person.getId(), person.getFirstName(), person.getLastName(), person.getEmail(), person.getPhone()))
                    .orElseThrow(() -> new IllegalArgumentException("No person found with ID: " + id));
            }
    }

    @Override
    public PersonDTO updatePerson(Integer id, PersonDTO personDTO) {
        if (id == null) {
            throw new IllegalArgumentException("Person ID cannot be null");
        } else if (personDTO == null) {
            throw new IllegalArgumentException("PersonDTO cannot be null");
        } else if (personDTO.getFirstName() == null || personDTO.getFirstName().isBlank()) {
            throw new IllegalArgumentException("Person first name cannot be null or blank");
        } else if (personDTO.getLastName() == null || personDTO.getLastName().isBlank()) {
            throw new IllegalArgumentException("Person last name cannot be null or blank");
        } else if (personDTO.getEmail() == null || personDTO.getEmail().isBlank()) {
            throw new IllegalArgumentException("Person email cannot be null or blank");
        } else if (!personRepository.existsById(id)) {
            throw new IllegalArgumentException("No person found with ID: " + id);
        } else {
            var existingEntity = personRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("No person found with ID: " + id));
            existingEntity.setFirstName(personDTO.getFirstName());
            existingEntity.setLastName(personDTO.getLastName());
            existingEntity.setEmail(personDTO.getEmail());
            existingEntity.setPhone(personDTO.getPhone());
            var savedEntity = personRepository.save(existingEntity);
            return new PersonDTO(savedEntity.getId(), savedEntity.getFirstName(), savedEntity.getLastName(), savedEntity.getEmail(), savedEntity.getPhone());
        }
    }
    
}
