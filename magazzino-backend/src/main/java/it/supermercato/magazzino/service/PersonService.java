package it.supermercato.magazzino.service;

import java.util.List;

import it.supermercato.magazzino.dto.PersonDTO;

public interface PersonService {
    
    List<PersonDTO> getAllPersons();

    PersonDTO createPerson(PersonDTO personDTO);

    PersonDTO getPersonById(Integer id);

    PersonDTO updatePerson(Integer id, PersonDTO personDTO);

    void deletePerson(Integer id);
}
