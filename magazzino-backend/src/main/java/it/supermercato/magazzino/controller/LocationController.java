package it.supermercato.magazzino.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.supermercato.magazzino.dto.LocationDTO;
import it.supermercato.magazzino.service.LocationService;


@RestController
@RequestMapping("/api/locations")
public class LocationController {
    
    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public List<LocationDTO> getAllLocations() {
        return locationService.getAllLocations();
    }
    
    @PostMapping
    @PreAuthorize("hasAuthority('Amministratore') or hasAuthority('Responsabile carico merci')")
    public ResponseEntity<Void> createLocation(@RequestBody LocationDTO locationDTO) {
        locationService.createLocation(locationDTO);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{area}/{aisle}/{shelf}")
    @PreAuthorize("hasAuthority('Amministratore') or hasAuthority('Responsabile carico merci')")
    public ResponseEntity<Void> deleteLocation(@PathVariable String area, @PathVariable Integer aisle, @PathVariable Integer shelf) {
        LocationDTO locationDTO = new LocationDTO(area, aisle, shelf);
        locationService.deleteLocation(locationDTO);
        return ResponseEntity.noContent().build();
    }
}
