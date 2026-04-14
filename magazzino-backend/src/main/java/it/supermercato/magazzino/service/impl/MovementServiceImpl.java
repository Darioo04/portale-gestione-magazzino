package it.supermercato.magazzino.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import it.supermercato.magazzino.dto.MovementDTO;
import it.supermercato.magazzino.dto.NotificationDTO;
import it.supermercato.magazzino.entity.Movement;
import it.supermercato.magazzino.entity.NotificationPriority;
import it.supermercato.magazzino.entity.Product;
import it.supermercato.magazzino.entity.User;
import it.supermercato.magazzino.repository.MovementRepository;
import it.supermercato.magazzino.repository.ProductRepository;
import it.supermercato.magazzino.repository.UserRepository;
import it.supermercato.magazzino.service.MovementService;
import it.supermercato.magazzino.service.NotificationService;

@Service
public class MovementServiceImpl implements MovementService {

    private final MovementRepository movementRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public MovementServiceImpl(MovementRepository movementRepository, ProductRepository productRepository, UserRepository userRepository, NotificationService notificationService) {
        this.movementRepository = movementRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Override
    public List<MovementDTO> getAllMovements() {
        return movementRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public MovementDTO getMovementById(Integer id) {
        return movementRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("No movement found with ID: " + id));
    }

    /**
     * Dedicated method for Loading goods (Carico Merci).
     * Bypasses standard Create mechanism to strictly enforce a positive quantity constraint.
     * This ensures the DB state is consistent with the logical 'Load' operation requested by Admins/Managers.
     */
    @Override
    public MovementDTO loadMovement(MovementDTO movementDTO) {
        validateInput(movementDTO);
        if (movementDTO.getQuantity() <= 0) {
            throw new IllegalArgumentException("Load movement requires a positive quantity.");
        }
        return saveNewMovement(movementDTO);
    }

    /**
     * Dedicated method for Unloading goods (Scarico Merci).
     * Strictly enforces a negative quantity constraint.
     * This separates responsibilities so that 'Department Workers' can only call this specific endpoint.
     */
    @Override
    public MovementDTO unloadMovement(MovementDTO movementDTO) {
        validateInput(movementDTO);
        if (movementDTO.getQuantity() >= 0) {
            throw new IllegalArgumentException("Unload movement requires a negative quantity.");
        }
        return saveNewMovement(movementDTO);
    }

    private MovementDTO saveNewMovement(MovementDTO movementDTO) {
        Product product = productRepository.findById(movementDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        // Logic to update Product stock
        int newQuantity = product.getQuantity() + movementDTO.getQuantity();
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Insufficient stock for this operation. Current stock: " + product.getQuantity() + ", Requested to change: " + movementDTO.getQuantity());
        }
        
        product.setQuantity(newQuantity);
        productRepository.save(product); // Assuming Spring Data JPA transaction handles this or explicit save

        checkStockAndNotify(product); // Check if we need to send low stock notifications after the update

        User user = userRepository.findById(movementDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        LocalDateTime movementDate = movementDTO.getMovementDate() != null ? movementDTO.getMovementDate() : LocalDateTime.now();

        Movement newEntity = new Movement(movementDate, movementDTO.getQuantity(), product, user);
        Movement savedEntity = movementRepository.save(newEntity);

        return toDTO(savedEntity);
    }

    private void checkStockAndNotify(Product product) {
        if (product.getQuantity() < product.getProductType().getStockThreshold()) {
            List<User> admins = userRepository.findByRole("Amministratore").stream().toList();

            String message = "Low stock alert for product: " + product.getProductType().getName() + " (EAN: " + product.getProductType().getEanCode() + "). Current stock: " + product.getQuantity();

            for (User admin : admins) {
                NotificationDTO notificationDTO = new NotificationDTO();
                notificationDTO.setMessage(message);
                notificationDTO.setPriority(NotificationPriority.HIGH);
                notificationDTO.setCreationDate(LocalDateTime.now());
                notificationDTO.setUserId(admin.getId());

                notificationService.createNotification(notificationDTO);
            }
        }
    }

    @Override
    public MovementDTO updateMovement(Integer id, MovementDTO movementDTO) {
        Movement existingMovement = movementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No movement found with ID: " + id));

        if (movementDTO.getMovementDate() != null) {
            existingMovement.setMovementDate(movementDTO.getMovementDate());
        }

        if (movementDTO.getQuantity() != null) {
            if (movementDTO.getQuantity() == 0) {
                 throw new IllegalArgumentException("Quantity cannot be 0");
            }
            existingMovement.setQuantity(movementDTO.getQuantity());
        }

        if (movementDTO.getProductId() != null) {
            Product product = productRepository.findById(movementDTO.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            existingMovement.setProduct(product);
        }

        if (movementDTO.getUserId() != null) {
            User user = userRepository.findById(movementDTO.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            existingMovement.setUser(user);
        }

        Movement savedEntity = movementRepository.save(existingMovement);
        return toDTO(savedEntity);
    }

    @Override
    public void deleteMovement(Integer id) {
        if (!movementRepository.existsById(id)) {
            throw new IllegalArgumentException("No movement found with ID: " + id);
        }
        movementRepository.deleteById(id);
    }

    @Override
    public List<MovementDTO> getMovementsByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        return movementRepository.findByMovementDateBetween(startOfDay, endOfDay).stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<MovementDTO> getMovementsByDateRange(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startOfDay = startDate.atStartOfDay();
        LocalDateTime endOfDay = endDate.atTime(LocalTime.MAX);
        return movementRepository.findByMovementDateBetween(startOfDay, endOfDay).stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<MovementDTO> getMovementsByUser(Integer userId) {
        return movementRepository.findByUserId(userId).stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<MovementDTO> getMovementsByProduct(Integer productId) {
        return movementRepository.findByProductId(productId).stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public List<MovementDTO> getMovementsByProductType(String eanCode) {
        return movementRepository.findByProductProductTypeEanCode(eanCode).stream()
                .map(this::toDTO)
                .toList();
    }

    private void validateInput(MovementDTO dto) {
        if (dto == null) throw new IllegalArgumentException("MovementDTO cannot be null");
        if (dto.getQuantity() == null || dto.getQuantity() == 0) throw new IllegalArgumentException("Quantity is required and cannot be 0");
        if (dto.getProductId() == null) throw new IllegalArgumentException("Product ID is required");
        if (dto.getUserId() == null) throw new IllegalArgumentException("User ID is required");
    }

    /**
     * Maps the rich Movement Entity back into a flat MovementDTO.
     * Extracts values from internal lazily-loaded relationships (Product, ProductType, User, Person)
     * creating a read-only projection (e.g. usernames, product ean codes) useful for grids and tables on the UI.
     */
    private MovementDTO toDTO(Movement entity) {
        return new MovementDTO(
                entity.getId(),
                entity.getMovementDate(),
                entity.getQuantity(),
                entity.getProduct().getId(),
                entity.getUser().getId(),
                // Populating additional read-only details
                entity.getProduct().getProductType().getName(),
                entity.getProduct().getProductType().getBrand(),
                entity.getProduct().getProductType().getEanCode(),
                entity.getUser().getUsername(),
                entity.getUser().getPerson().getFirstName(),
                entity.getUser().getPerson().getLastName()
        );
    }
}
