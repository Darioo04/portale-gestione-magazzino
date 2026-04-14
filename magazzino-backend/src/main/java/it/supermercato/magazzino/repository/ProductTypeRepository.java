package it.supermercato.magazzino.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.supermercato.magazzino.entity.ProductType;

public interface ProductTypeRepository extends JpaRepository<ProductType, String> {
    
    boolean existsByEanCode(String eanCode);

    Optional<ProductType> findByEanCode(String eanCode);

    void deleteByEanCode(String eanCode);
}
