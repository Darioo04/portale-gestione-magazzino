package it.supermercato.magazzino.repository;

import java.util.List;
import java.util.Optional;

import it.supermercato.magazzino.entity.Product;

public interface ProductRepository {
    
    List<Product> findAll();

    Optional<Product> findById(Integer id);

    boolean existsById(Integer id);

    Product save(Product product);

    void deleteById(Integer id);
}
