package it.supermercato.magazzino.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.access.prepost.PreAuthorize;

import it.supermercato.magazzino.dto.MovementDTO;
import it.supermercato.magazzino.service.MovementService;

@RestController
@RequestMapping("/api/movements")
public class MovementController {

    private final MovementService movementService;

    public MovementController(MovementService movementService) {
        this.movementService = movementService;
    }

    @GetMapping
    public ResponseEntity<List<MovementDTO>> getAllMovements() {
        return ResponseEntity.ok(movementService.getAllMovements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovementDTO> getMovementById(@PathVariable Integer id) {
        return ResponseEntity.ok(movementService.getMovementById(id));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<MovementDTO>> getMovementsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(movementService.getMovementsByDate(date));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<MovementDTO>> getMovementsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(movementService.getMovementsByDateRange(startDate, endDate));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<MovementDTO>> getMovementsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(movementService.getMovementsByUser(userId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<MovementDTO>> getMovementsByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(movementService.getMovementsByProduct(productId));
    }

    @GetMapping("/product-type/{eanCode}")
    public ResponseEntity<List<MovementDTO>> getMovementsByProductType(@PathVariable String eanCode) {
        return ResponseEntity.ok(movementService.getMovementsByProductType(eanCode));
    }

    @PostMapping("/load")
    @PreAuthorize("hasAuthority('Amministratore') or hasAuthority('Responsabile carico merci')")
    public ResponseEntity<MovementDTO> loadMovement(@RequestBody MovementDTO movementDTO) {
        return ResponseEntity.status(201).body(movementService.loadMovement(movementDTO));
    }

    @PostMapping("/unload")
    @PreAuthorize("hasAuthority('Amministratore') or hasAuthority('Addetto di reparto')")
    public ResponseEntity<?> unloadMovement(@RequestBody MovementDTO movementDTO) {
        try {
            MovementDTO result = movementService.unloadMovement(movementDTO);
            return ResponseEntity.status(201).body(result);
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Amministratore')")
    public ResponseEntity<MovementDTO> updateMovement(@PathVariable Integer id, @RequestBody MovementDTO movementDTO) {
        return ResponseEntity.ok(movementService.updateMovement(id, movementDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Amministratore')")
    public ResponseEntity<Void> deleteMovement(@PathVariable Integer id) {
        movementService.deleteMovement(id);
        return ResponseEntity.noContent().build();
    }
}
