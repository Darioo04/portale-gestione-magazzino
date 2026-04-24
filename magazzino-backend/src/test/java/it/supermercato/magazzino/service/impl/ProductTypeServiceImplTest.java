package it.supermercato.magazzino.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import it.supermercato.magazzino.dto.ProductTypeDTO;
import it.supermercato.magazzino.entity.Category;
import it.supermercato.magazzino.entity.ProductType;
import it.supermercato.magazzino.repository.CategoryRepository;
import it.supermercato.magazzino.repository.ProductTypeRepository;

@ExtendWith(MockitoExtension.class)
public class ProductTypeServiceImplTest {

    @Mock
    private ProductTypeRepository productTypeRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductTypeServiceImpl productTypeService;

    private ProductTypeDTO testDto;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testDto = new ProductTypeDTO("8001234567890", "Pasta", "BrandX", new BigDecimal("1.50"), 10, 1);
        testCategory = new Category("Alimentari");
        ReflectionTestUtils.setField(testCategory, "id", 1);
    }

    @Test
    void testCreateProductType_Success() {
        when(productTypeRepository.existsByEanCode(anyString())).thenReturn(false);
        when(categoryRepository.findById(1)).thenReturn(Optional.of(testCategory));
        
        ProductType savedEntity = new ProductType(
            testDto.getEanCode(), testDto.getName(), testDto.getBrand(), 
            testDto.getPrice(), testDto.getStockThreshold(), testCategory
        );
        when(productTypeRepository.save(any(ProductType.class))).thenReturn(savedEntity);

        ProductTypeDTO result = productTypeService.createProductType(testDto);

        assertNotNull(result);
        assertEquals("Pasta", result.getName());
        verify(productTypeRepository).save(any(ProductType.class));
    }

    @Test
    void testCreateProductType_AlreadyExists() {
        when(productTypeRepository.existsByEanCode("8001234567890")).thenReturn(true);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            productTypeService.createProductType(testDto);
        });

        assertEquals("Product type with EAN code 8001234567890 already exists.", thrown.getMessage());
    }
}
