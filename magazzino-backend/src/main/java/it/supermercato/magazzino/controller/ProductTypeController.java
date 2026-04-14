package it.supermercato.magazzino.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.supermercato.magazzino.dto.ProductTypeDTO;
import it.supermercato.magazzino.service.ProductTypeService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/product-types")
public class ProductTypeController {
    
    private final ProductTypeService productTypeService;

    public ProductTypeController(ProductTypeService productTypeService) {
        this.productTypeService = productTypeService;
    }

    @GetMapping
    public List<ProductTypeDTO> getAllProductTypes() {
        return productTypeService.getAllProductTypes();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('Amministratore')")
    public ProductTypeDTO createProductType(@RequestBody ProductTypeDTO productTypeDTO) {
        return productTypeService.createProductType(productTypeDTO);
    }

    @GetMapping("/{eanCode}")
    public ProductTypeDTO getProductTypeByEanCode(@PathVariable String eanCode) {
        return productTypeService.getProductTypeByEanCode(eanCode);
    }

    @DeleteMapping("/{eanCode}")
    @PreAuthorize("hasAuthority('Amministratore')")
    public void deleteProductType(@PathVariable String eanCode) {
        productTypeService.deleteProductType(eanCode);
    }
}
