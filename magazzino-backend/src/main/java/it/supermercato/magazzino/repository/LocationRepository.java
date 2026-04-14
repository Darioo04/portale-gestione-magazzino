package it.supermercato.magazzino.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.supermercato.magazzino.entity.Location;
import it.supermercato.magazzino.entity.Location.LocationId;

@Repository
public interface LocationRepository extends JpaRepository<Location, LocationId> {
    
}
