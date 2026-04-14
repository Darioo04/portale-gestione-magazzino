package it.supermercato.magazzino.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import it.supermercato.magazzino.dto.ProductDTO;
import it.supermercato.magazzino.entity.Location;
import it.supermercato.magazzino.entity.Product;
import it.supermercato.magazzino.entity.ProductType;
import it.supermercato.magazzino.repository.LocationRepository;
import it.supermercato.magazzino.repository.ProductRepository;
import it.supermercato.magazzino.repository.ProductTypeRepository;
import it.supermercato.magazzino.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductTypeRepository productTypeRepository;
    private final LocationRepository locationRepository;

    public ProductServiceImpl(ProductTypeRepository productTypeRepository,
                              LocationRepository locationRepository, 
                              ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.productTypeRepository = productTypeRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public ProductDTO getProductById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }
        return productRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("No product found with ID: " + id));
    }

    /**
     * Creates a new Product. 
     * Complex logic included:
     * - Validates EAN Code, Quantity, and Batch.
     * - Resolves the parent ProductType dynamically based on the EAN code string.
     * - Resolves the nullable structured Location by combining physical Area, Aisle, and Shelf parameters.
     */
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        if (productDTO == null) {
            throw new IllegalArgumentException("ProductDTO cannot be null");
        } else if (productDTO.getEanCode() == null || productDTO.getEanCode().isBlank()) {
            throw new IllegalArgumentException("EAN code cannot be null or blank");
        } else if (productDTO.getQuantity() == null || productDTO.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be null or negative");
        } else if (productDTO.getBatch() == null || productDTO.getBatch().isBlank()) {
            throw new IllegalArgumentException("Batch cannot be null or blank");
        }

        // Resolve the ProductType by EAN code
        ProductType productType = productTypeRepository.findByEanCode(productDTO.getEanCode())
                .orElseThrow(() -> new IllegalArgumentException("Product type with EAN code " + productDTO.getEanCode() + " not found."));

        // Resolve the Location (nullable as per DB schema: ON DELETE SET NULL)
        Location location = resolveLocation(productDTO);

        var newEntity = new Product(productDTO.getQuantity(), productDTO.getExpirationDate(), productDTO.getBatch(), productType, location);
        var savedEntity = productRepository.save(newEntity);

        return toDTO(savedEntity);
    }

    @Override
    public ProductDTO updateProduct(Integer id, ProductDTO productDTO) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        } else if (productDTO == null) {
            throw new IllegalArgumentException("ProductDTO cannot be null");
        } else if (productDTO.getQuantity() == null || productDTO.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be null or negative");
        } else if (productDTO.getBatch() == null || productDTO.getBatch().isBlank()) {
            throw new IllegalArgumentException("Batch cannot be null or blank");
        }

        var existingEntity = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No product found with ID: " + id));

        // If EAN code is provided and different, resolve the new ProductType
        if (productDTO.getEanCode() != null && !productDTO.getEanCode().isBlank()) {
            ProductType productType = productTypeRepository.findByEanCode(productDTO.getEanCode())
                    .orElseThrow(() -> new IllegalArgumentException("Product type with EAN code " + productDTO.getEanCode() + " not found."));
            existingEntity.setProductType(productType);
        }

        existingEntity.setQuantity(productDTO.getQuantity());
        existingEntity.setExpirationDate(productDTO.getExpirationDate());
        existingEntity.setBatch(productDTO.getBatch());

        // Resolve and update the Location
        Location location = resolveLocation(productDTO);
        existingEntity.setLocation(location);

        var savedEntity = productRepository.save(existingEntity);
        return toDTO(savedEntity);
    }

    @Override
    public void deleteProduct(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        } else if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("No product found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

    /**
     * Resolves a Location from the DTO's flat composite key fields (Area, Aisle, Shelf).
     * Builds the @Embeddable LocationId and searches the Entity.
     * Returns null if no location fields are provided, maintaining compatibility with the DB schema where Location is nullable (ON DELETE SET NULL).
     */
    private Location resolveLocation(ProductDTO dto) {
        if (dto.getLocationArea() != null && dto.getLocationAisle() != null && dto.getLocationShelf() != null) {
            var locationId = new Location.LocationId();
            locationId.setArea(dto.getLocationArea());
            locationId.setAisle(dto.getLocationAisle());
            locationId.setShelf(dto.getLocationShelf());

            return locationRepository.findById(locationId)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No location found with area: " + dto.getLocationArea() +
                            ", aisle: " + dto.getLocationAisle() +
                            ", shelf: " + dto.getLocationShelf()));
        }
        return null;  // location is nullable in the DB schema
    }

    /**
     * Converts a Product entity to a ProductDTO, including read-only fields from ProductType and Location.
     */
    private ProductDTO toDTO(Product entity) {
        String locationArea = null;
        Integer locationAisle = null;
        Integer locationShelf = null;

        if (entity.getLocation() != null) {
            locationArea = entity.getLocation().getId().getArea();
            locationAisle = entity.getLocation().getId().getAisle();
            locationShelf = entity.getLocation().getId().getShelf();
        }

        return new ProductDTO(
                entity.getId(),
                entity.getProductType().getEanCode(),
                entity.getQuantity(),
                entity.getExpirationDate(),
                entity.getBatch(),
                entity.getProductType().getName(),      // productName (read-only)
                entity.getProductType().getBrand(),      // brand (read-only)
                locationArea,
                locationAisle,
                locationShelf
        );
    }
}
