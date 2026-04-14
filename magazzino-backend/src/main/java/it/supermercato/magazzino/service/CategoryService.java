package it.supermercato.magazzino.service;

import java.util.List;

import it.supermercato.magazzino.dto.CategoryDTO;

public interface CategoryService {
    
    List<CategoryDTO> getAllCategories();

    CategoryDTO createCategory(CategoryDTO categoryDTO);
}
