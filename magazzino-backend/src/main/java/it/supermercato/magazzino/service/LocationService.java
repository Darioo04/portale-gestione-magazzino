package it.supermercato.magazzino.service;

import java.util.List;

import it.supermercato.magazzino.dto.LocationDTO;

public interface LocationService {

    List<LocationDTO> getAllLocations();
    
    void createLocation(LocationDTO locationDTO);
    
    void deleteLocation(LocationDTO locationDTO);
}
