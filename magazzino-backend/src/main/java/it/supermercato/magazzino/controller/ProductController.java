package it.supermercato.magazzino.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.access.prepost.PreAuthorize;

import it.supermercato.magazzino.dto.ProductDTO;
import it.supermercato.magazzino.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Endpoint to retrieve all products
     * Method: GET
     * Path: /api/products
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Endpoint to retrieve a product by its ID
     * Method: GET
     * Path: /api/products/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Integer id) {
        ProductDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Endpoint to create a new product
     * Method: POST
     * Path: /api/products
     * Request Body: JSON representation of ProductDTO
     */
    @PostMapping
    @PreAuthorize("hasAuthority('Amministratore') or hasAuthority('Responsabile carico merci')")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.status(201).body(createdProduct);
    }

    /**
     * Endpoint to update an existing product
     * Method: PUT
     * Path: /api/products/{id}
     * Request Body: JSON representation of ProductDTO
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('Amministratore') or hasAuthority('Responsabile carico merci')")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Integer id, @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Endpoint to delete a product by its ID
     * Method: DELETE
     * Path: /api/products/{id}
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('Amministratore') or hasAuthority('Responsabile carico merci')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
