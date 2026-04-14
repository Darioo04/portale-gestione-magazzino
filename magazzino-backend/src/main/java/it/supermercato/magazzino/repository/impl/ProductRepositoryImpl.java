package it.supermercato.magazzino.repository.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import it.supermercato.magazzino.entity.Product;
import it.supermercato.magazzino.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 * Custom implementation for the ProductRepository.
 * Unlike other standard repositories that extend JpaRepository natively, 
 * this class demonstrates direct usage of the JPA @PersistenceContext EntityManager.
 * It manually performs queries, merges, removes, and manages transactions.
 */
@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Product> findAll() {
        return entityManager.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList();
    }

    @Override
    public Optional<Product> findById(Integer id) {
        Product product = entityManager.find(Product.class, id);
        return Optional.ofNullable(product);  // return an Optional containing the product if found, or empty if not found
    }

    @Override
    public boolean existsById(Integer id) {
        return entityManager.find(Product.class, id) != null;
    }

    @Override
    @Transactional
    public Product save(Product product) {
        if (product.getId() == null) {
            entityManager.persist(product);  // persist the new product and assign it an ID (INSERT)
            return product;
        } else {
            return entityManager.merge(product);  // merge the existing product (UPDATE) and return the managed instance
        }
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        if (id != null) {
            Product product = entityManager.find(Product.class, id);  // find the product by its ID
            if (product != null) {
                entityManager.remove(product);  // remove the product from the database
            }
        } else {
            throw new IllegalArgumentException("ID cannot be null for deletion.");
        }
    }
    
}
