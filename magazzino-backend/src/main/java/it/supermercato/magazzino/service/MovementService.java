package it.supermercato.magazzino.service;

import java.time.LocalDate;
import java.util.List;

import it.supermercato.magazzino.dto.MovementDTO;

public interface MovementService {

    List<MovementDTO> getAllMovements();

    MovementDTO getMovementById(Integer id);

    MovementDTO loadMovement(MovementDTO movementDTO);

    MovementDTO unloadMovement(MovementDTO movementDTO);

    MovementDTO updateMovement(Integer id, MovementDTO movementDTO);

    void deleteMovement(Integer id);

    List<MovementDTO> getMovementsByDate(LocalDate date);

    List<MovementDTO> getMovementsByDateRange(LocalDate startDate, LocalDate endDate);

    List<MovementDTO> getMovementsByUser(Integer userId);

    List<MovementDTO> getMovementsByProduct(Integer productId);

    List<MovementDTO> getMovementsByProductType(String eanCode);
}
