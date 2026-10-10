package com.oms.product_service.controller;

import com.oms.product_service.dto.CreateProductDto;
import com.oms.product_service.dto.ProductDto;
import com.oms.product_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> getActiveCatalog(){
        return ResponseEntity.ok(productService.getActiveCatalog());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id){
        return ResponseEntity.ok(productService.getById(id));
    }

    @GetMapping("/admin")
    public ResponseEntity<List<ProductDto>> getAllForAdmin(){
        return ResponseEntity.ok(productService.getAllForAdmin());
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody CreateProductDto createProductDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(createProductDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @Valid @RequestBody CreateProductDto updateProductDto){
        return ResponseEntity.ok(productService.updateProduct(id,updateProductDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id){
        productService.deactivate(id);
        return ResponseEntity.noContent().build();
    }
}
