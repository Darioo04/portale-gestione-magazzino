package it.supermercato.magazzino.service;

import java.util.List;

import it.supermercato.magazzino.dto.RoleDTO;

public interface RoleService {
    
    List<RoleDTO> getAllRoles();

    RoleDTO createRole(RoleDTO roleDTO);
}
