package it.supermercato.magazzino.service;

import java.util.List;

import it.supermercato.magazzino.dto.UserDTO;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Integer id);

    UserDTO createUser(UserDTO userDTO);

    UserDTO updateUser(Integer id, UserDTO userDTO);

    void deleteUser(Integer id);
}
