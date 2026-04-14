package it.supermercato.magazzino.service;

import java.util.List;

import it.supermercato.magazzino.dto.ProductTypeDTO;

public interface ProductTypeService {
    
    ProductTypeDTO createProductType(ProductTypeDTO productTypeDTO);
    ProductTypeDTO getProductTypeByEanCode(String eanCode);
    ProductTypeDTO updateProductType(String eanCode, ProductTypeDTO productTypeDTO);
    void deleteProductType(String eanCode);
    List<ProductTypeDTO> getAllProductTypes();
}
