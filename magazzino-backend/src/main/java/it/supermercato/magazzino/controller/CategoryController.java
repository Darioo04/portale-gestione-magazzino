package it.supermercato.magazzino.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.supermercato.magazzino.dto.CategoryDTO;
import it.supermercato.magazzino.service.CategoryService;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    // injection of the service layer into the controller, allowing the controller
    // to delegate business logic to the service
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Endpoint to retrieve all categories
     * Method: GET
     * Path: /api/categories
     */
    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        List<CategoryDTO> categories = categoryService.getAllCategories();

        // return a list of categories and an HTTP status code of 200 (OK)
        return ResponseEntity.ok(categories);
    }

    /**
     * Endpoint to create a new category
     * Method: POST
     * Path: /api/categories
     * Request Body: JSON representation of CategoryDTO
     */
    @PostMapping
    @PreAuthorize("hasAuthority('Amministratore')")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDTO) {
        CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);

        // return the created category and an HTTP status code of 201 (Created)
        return ResponseEntity.status(201).body(createdCategory);
    }

}
