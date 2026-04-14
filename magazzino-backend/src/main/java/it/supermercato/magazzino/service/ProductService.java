package it.supermercato.magazzino.service;

import java.util.List;

import it.supermercato.magazzino.dto.ProductDTO;

public interface ProductService {

    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(Integer id);

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Integer id, ProductDTO productDTO);

    void deleteProduct(Integer id);
}
