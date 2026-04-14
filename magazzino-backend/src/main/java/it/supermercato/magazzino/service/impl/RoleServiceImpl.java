package it.supermercato.magazzino.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import it.supermercato.magazzino.dto.RoleDTO;
import it.supermercato.magazzino.service.RoleService;
import it.supermercato.magazzino.entity.Role;
import it.supermercato.magazzino.repository.RoleRepository;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    
    /** 
     * @return List<RoleDTO>
     */
    @Override
    public List<RoleDTO> getAllRoles() {

        List<RoleDTO> roles = roleRepository.findAll().stream()
                .map(entity -> new RoleDTO(entity.getId(), entity.getName()))
                .toList();
        return roles;

    }

    /** 
     * @param roleDTO
     * @return RoleDTO
     */
    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        
        if (roleRepository.existsByName(roleDTO.getName())) {
            throw new IllegalArgumentException("A role with the name '" + roleDTO.getName() + "' already exists.");
        }

        var newEntity = new Role(roleDTO.getName());

        var savedEntity = roleRepository.save(newEntity);

        return new RoleDTO(savedEntity.getId(), savedEntity.getName());
    }
}
