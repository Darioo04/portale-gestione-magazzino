package it.supermercato.magazzino.service.impl;

import it.supermercato.magazzino.dto.CategoryDTO;
import it.supermercato.magazzino.service.CategoryService;
import it.supermercato.magazzino.entity.Category;
import it.supermercato.magazzino.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /** 
     * @return List<CategoryDTO>
     */
    @Override
    public List<CategoryDTO> getAllCategories() {
        // retrieve all Category entities from the database using the repository
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(entity -> new CategoryDTO(entity.getId(), entity.getName()))  // convert each Category entity to a CategoryDTO using a stream and the map function
                .collect(Collectors.toList());                                     // collect the results into a List of CategoryDTOs and return it
    }

    /** 
     * @param dto
     * @return CategoryDTO
     */
    @Override
    public CategoryDTO createCategory(CategoryDTO dto) {
        if (categoryRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("A category with the name '" + dto.getName() + "' already exists.");
        }

        Category newEntity = new Category(dto.getName());  // create a new Category entity using the name from the provided CategoryDTO

        Category savedEntity = categoryRepository.save(newEntity);  // save the new Category entity to the database and return the saved entity, which includes the generated ID

        return new CategoryDTO(savedEntity.getId(), savedEntity.getName());  // convert the saved Category entity back to a CategoryDTO and return it
    }
}
