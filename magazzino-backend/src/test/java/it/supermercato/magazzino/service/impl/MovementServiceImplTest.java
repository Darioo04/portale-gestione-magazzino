package it.supermercato.magazzino.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import it.supermercato.magazzino.dto.MovementDTO;
import it.supermercato.magazzino.entity.Movement;
import it.supermercato.magazzino.entity.Person;
import it.supermercato.magazzino.entity.Product;
import it.supermercato.magazzino.entity.ProductType;
import it.supermercato.magazzino.entity.User;
import it.supermercato.magazzino.repository.MovementRepository;
import it.supermercato.magazzino.repository.ProductRepository;
import it.supermercato.magazzino.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class MovementServiceImplTest {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MovementServiceImpl movementService;

    private MovementDTO movementDto;
    private Product testProduct;
    private User testUser;
    private ProductType testType;

    @BeforeEach
    void setUp() {
        movementDto = new MovementDTO(null, LocalDateTime.now(), 50, 10, 1, null, null, null, null, null, null);
        
        testType = new ProductType();
        testType.setEanCode("123");
        testType.setName("Test Product");
        testType.setStockThreshold(10);
        
        testProduct = new Product();
        testProduct.setId(10);
        testProduct.setQuantity(20);
        testProduct.setProductType(testType);
        
        testUser = new User();
        ReflectionTestUtils.setField(testUser, "id", 1);
        testUser.setUsername("mario");

        Person person = new Person();
        person.setFirstName("Mario");
        person.setLastName("Rossi");
        testUser.setPerson(person);
    }

    @Test
    void testLoadMovement_Success() {
        when(productRepository.findById(10)).thenReturn(Optional.of(testProduct));
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(movementRepository.save(any(Movement.class))).thenAnswer(i -> i.getArguments()[0]);

        MovementDTO result = movementService.loadMovement(movementDto);

        assertNotNull(result);
        assertEquals(50, result.getQuantity());
        assertEquals(70, testProduct.getQuantity()); // 20 + 50
        verify(movementRepository).save(any(Movement.class));
    }

    @Test
    void testUnloadMovement_Success() {
        movementDto.setQuantity(-10); // Trying to unload 10
        when(productRepository.findById(10)).thenReturn(Optional.of(testProduct));
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(movementRepository.save(any(Movement.class))).thenAnswer(i -> i.getArguments()[0]);

        MovementDTO result = movementService.unloadMovement(movementDto);

        assertNotNull(result);
        assertEquals(-10, result.getQuantity());
        assertEquals(10, testProduct.getQuantity()); // 20 - 10
    }

    @Test
    void testUnloadMovement_InsufficientStock() {
        movementDto.setQuantity(-30); // Trying to unload 30, but only 20 available
        when(productRepository.findById(10)).thenReturn(Optional.of(testProduct));

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            movementService.unloadMovement(movementDto);
        });

        assertEquals("Insufficient stock for this operation. Current stock: 20, Requested to change: -30", thrown.getMessage());
    }
}
