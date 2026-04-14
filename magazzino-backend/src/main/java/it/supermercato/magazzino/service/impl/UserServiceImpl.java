package it.supermercato.magazzino.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import it.supermercato.magazzino.dto.UserDTO;
import it.supermercato.magazzino.entity.Person;
import it.supermercato.magazzino.entity.Role;
import it.supermercato.magazzino.entity.User;
import it.supermercato.magazzino.repository.PersonRepository;
import it.supermercato.magazzino.repository.RoleRepository;
import it.supermercato.magazzino.repository.UserRepository;
import it.supermercato.magazzino.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, PersonRepository personRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public UserDTO getUserById(Integer id) {
        return userRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("No user found with ID: " + id));
    }

    /**
     * Creates a new User.
     * Enforces domain rules:
     * - A username must be globally unique.
     * - A Person can only be associated with ONE User account (1-to-1 logical constraint).
     * Rejects the creation with IllegalArgumentException if any of these invariants are violated.
     */
    @Override
    public UserDTO createUser(UserDTO userDTO) {
        validateInput(userDTO);

        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new IllegalArgumentException("Username already exists.");
        }
        
        if (userRepository.existsByPersonId(userDTO.getPersonId())) {
            throw new IllegalArgumentException("Person already has a user account linked.");
        }

        Person person = personRepository.findById(userDTO.getPersonId())
                .orElseThrow(() -> new IllegalArgumentException("Person not found"));
        
        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        User newEntity = new User(userDTO.getUsername(), userDTO.getPassword(), person, role);
        User savedEntity = userRepository.save(newEntity);

        return toDTO(savedEntity);
    }

    @Override
    public UserDTO updateUser(Integer id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No user found with ID: " + id));

        // Note: Password can be empty if we don't want to update it
        if (userDTO.getUsername() != null && !userDTO.getUsername().isBlank()) {
            if (!existingUser.getUsername().equals(userDTO.getUsername()) && userRepository.existsByUsername(userDTO.getUsername())) {
                throw new IllegalArgumentException("Username already exists.");
            }
            existingUser.setUsername(userDTO.getUsername());
        }

        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            existingUser.setPassword(userDTO.getPassword());
        }

        if (userDTO.getPersonId() != null) {
             if (!existingUser.getPerson().getId().equals(userDTO.getPersonId()) && userRepository.existsByPersonId(userDTO.getPersonId())) {
                throw new IllegalArgumentException("Person already has a user account linked.");
            }
            Person person = personRepository.findById(userDTO.getPersonId())
                    .orElseThrow(() -> new IllegalArgumentException("Person not found"));
            existingUser.setPerson(person);
        }

        if (userDTO.getRoleId() != null) {
            Role role = roleRepository.findById(userDTO.getRoleId())
                    .orElseThrow(() -> new IllegalArgumentException("Role not found"));
            existingUser.setRole(role);
        }

        User savedEntity = userRepository.save(existingUser);
        
        return toDTO(savedEntity);
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("No user found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    private void validateInput(UserDTO userDTO) {
        if (userDTO == null) throw new IllegalArgumentException("UserDTO cannot be null");
        if (userDTO.getUsername() == null || userDTO.getUsername().isBlank()) throw new IllegalArgumentException("Username is required");
        if (userDTO.getPassword() == null || userDTO.getPassword().isBlank()) throw new IllegalArgumentException("Password is required");
        if (userDTO.getPersonId() == null) throw new IllegalArgumentException("Person ID is required");
        if (userDTO.getRoleId() == null) throw new IllegalArgumentException("Role ID is required");
    }

    private UserDTO toDTO(User entity) {
        // Password is set to null before returning to ensure it's not exposed,
        // even though @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) is in place.
        return new UserDTO(
                entity.getId(),
                entity.getUsername(),
                null,
                entity.getPerson().getId(),
                entity.getRole().getId()
        );
    }
}
