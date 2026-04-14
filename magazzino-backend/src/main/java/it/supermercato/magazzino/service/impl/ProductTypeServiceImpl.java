package it.supermercato.magazzino.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import it.supermercato.magazzino.dto.ProductTypeDTO;
import it.supermercato.magazzino.service.ProductTypeService;
import it.supermercato.magazzino.entity.ProductType;
import it.supermercato.magazzino.entity.Category;
import it.supermercato.magazzino.repository.CategoryRepository;
import it.supermercato.magazzino.repository.ProductTypeRepository;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    private final ProductTypeRepository productTypeRepository;
    private final CategoryRepository categoryRepository;

    public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository, CategoryRepository categoryRepository) {
        this.productTypeRepository = productTypeRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ProductTypeDTO createProductType(ProductTypeDTO productTypeDTO) {
        if (productTypeRepository.existsByEanCode(productTypeDTO.getEanCode())) {
            throw new IllegalArgumentException("Product type with EAN code " + productTypeDTO.getEanCode() + " already exists.");
        } else {
            Category category = categoryRepository.findById(productTypeDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category with ID " + productTypeDTO.getCategoryId() + " not found."));
            var newEntity = new ProductType(productTypeDTO.getEanCode(), productTypeDTO.getName(), productTypeDTO.getBrand(), productTypeDTO.getPrice(), productTypeDTO.getStockThreshold(), category);
            var savedEntity = productTypeRepository.save(newEntity);
            return new ProductTypeDTO(savedEntity.getEanCode(), savedEntity.getName(), savedEntity.getBrand(), savedEntity.getPrice(), savedEntity.getStockThreshold(), savedEntity.getCategory().getId());
        }
    }

    @Override
    public ProductTypeDTO getProductTypeByEanCode(String eanCode) {
        if (eanCode == null || eanCode.isBlank()) {
            throw new IllegalArgumentException("EAN code cannot be null or blank");
        } else {
            ProductType productType = productTypeRepository.findByEanCode(eanCode)
                .orElseThrow(() -> new IllegalArgumentException("Product type with EAN code " + eanCode + " not found."));
            return new ProductTypeDTO(productType.getEanCode(), productType.getName(), productType.getBrand(), productType.getPrice(), productType.getStockThreshold(), productType.getCategory().getId());
        }
    }

    @Override
    public ProductTypeDTO updateProductType(String eanCode, ProductTypeDTO productTypeDTO) {
        if (eanCode == null || eanCode.isBlank()) {
            throw new IllegalArgumentException("EAN code cannot be null or blank");
        } else if (!productTypeRepository.existsByEanCode(eanCode)) {
            throw new IllegalArgumentException("Product type with EAN code " + eanCode + " not found.");
        } else {
            Category category = categoryRepository.findById(productTypeDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category with ID " + productTypeDTO.getCategoryId() + " not found."));
            var existingEntity = productTypeRepository.findByEanCode(eanCode).get();
            existingEntity.setName(productTypeDTO.getName());
            existingEntity.setBrand(productTypeDTO.getBrand());
            existingEntity.setPrice(productTypeDTO.getPrice());
            existingEntity.setStockThreshold(productTypeDTO.getStockThreshold());
            existingEntity.setCategory(category);
            var savedEntity = productTypeRepository.save(existingEntity);
            return new ProductTypeDTO(savedEntity.getEanCode(), savedEntity.getName(), savedEntity.getBrand(), savedEntity.getPrice(), savedEntity.getStockThreshold(), savedEntity.getCategory().getId());
        }
    }

    @Override
    public void deleteProductType(String eanCode) {
        if (eanCode == null || eanCode.isBlank()) {
            throw new IllegalArgumentException("EAN code cannot be null or blank");
        } else if (!productTypeRepository.existsByEanCode(eanCode)) {
            throw new IllegalArgumentException("Product type with EAN code " + eanCode + " not found.");
        } else {
            productTypeRepository.deleteByEanCode(eanCode);
        }
    }

    @Override
    public List<ProductTypeDTO> getAllProductTypes() {
        return productTypeRepository.findAll().stream()
            .map(pt -> new ProductTypeDTO(pt.getEanCode(), pt.getName(), pt.getBrand(), pt.getPrice(), pt.getStockThreshold(), pt.getCategory().getId()))
            .toList();
    }
    
}
