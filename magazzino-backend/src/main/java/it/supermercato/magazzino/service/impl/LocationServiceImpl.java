package it.supermercato.magazzino.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import it.supermercato.magazzino.dto.LocationDTO;
import it.supermercato.magazzino.service.LocationService;
import it.supermercato.magazzino.entity.Location;
import it.supermercato.magazzino.repository.LocationRepository;

@Service
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public void createLocation(LocationDTO locationDTO) {
        
        var newEntity = new Location(
            locationDTO.getArea(),
            locationDTO.getAisle(),
            locationDTO.getShelf()
        );

        locationRepository.save(newEntity);
    }

    @Override
    public void deleteLocation(LocationDTO locationDTO) {
        var locationId = new Location.LocationId();
        locationId.setArea(locationDTO.getArea());
        locationId.setAisle(locationDTO.getAisle());
        locationId.setShelf(locationDTO.getShelf());

        if (!locationRepository.existsById(locationId)) {
            throw new IllegalArgumentException("No location found with area: " + locationDTO.getArea() + ", aisle: " + locationDTO.getAisle() + ", shelf: " + locationDTO.getShelf());
        }

        locationRepository.deleteById(locationId);
    }

    @Override
    public List<LocationDTO> getAllLocations() {
        List<LocationDTO> locations = locationRepository.findAll().stream()
                .map(entity -> new LocationDTO(
                    entity.getId().getArea(),
                    entity.getId().getAisle(),
                    entity.getId().getShelf()
                ))
                .toList();
        return locations;
    }
    
}
