package it.supermercato.magazzino.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.supermercato.magazzino.entity.Movement;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Integer> {
    
    List<Movement> findByUserId(Integer userId);

    List<Movement> findByProductId(Integer productId);

    List<Movement> findByProductProductTypeEanCode(String eanCode);

    List<Movement> findByMovementDateBetween(LocalDateTime start, LocalDateTime end);
}
