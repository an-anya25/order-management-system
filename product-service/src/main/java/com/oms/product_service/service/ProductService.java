package com.oms.product_service.service;

import com.oms.product_service.dto.CreateProductDto;
import com.oms.product_service.dto.ProductDto;
import com.oms.product_service.entity.Product;
import com.oms.product_service.exception.DuplicateProductNameException;
import com.oms.product_service.exception.ProductNotFoundException;
import com.oms.product_service.repository.ProductRepository;
import com.oms.product_service.security.JwtPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private boolean isAdmin(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal instanceof JwtPrincipal jwtPrincipal && "ADMIN".equals(jwtPrincipal.role());
    }

    public List<ProductDto> getActiveCatalog() {
        return productRepository.findAllByActiveTrueOrderByNameAsc().stream()
                .map(this::mapProductToProductDto)
                .toList();
    }

    public ProductDto getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found with id: "+id));

        if(!product.getActive() && !isAdmin()){
            throw new ProductNotFoundException("Product not found with id: "+id);
        }

        return mapProductToProductDto(product);
    }

    public List<ProductDto> getAllForAdmin() {
        return productRepository.findAll().stream()
                .sorted((a,b)-> a.getName().compareToIgnoreCase(b.getName()))
                .map(this::mapProductToProductDto)
                .toList();
    }

    @Transactional
    public ProductDto createProduct(CreateProductDto createProductDto) {
        String name = createProductDto.getName().trim();
        if(productRepository.existsByNameIgnoreCase(name)){
            throw new DuplicateProductNameException("A product with this name already exist");
        }

        Product product = new Product();

        product.setName(createProductDto.getName());
        product.setPrice(createProductDto.getPrice());
        product.setActive(createProductDto.getActive()!=null? createProductDto.getActive():true);

        return mapProductToProductDto(productRepository.save(product));

    }

    @Transactional
    public ProductDto updateProduct(Long id, CreateProductDto updateProductDto) {
        Product product = productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product not found with id: "+id));

        String name = updateProductDto.getName().trim();
        if(productRepository.existsByNameIgnoreCaseAndIdNot(name,id)){
            throw new DuplicateProductNameException("A product with this name already exist");
        }

        product.setName(updateProductDto.getName());
        product.setPrice(updateProductDto.getPrice());

        if(updateProductDto.getActive() !=null){
            product.setActive(updateProductDto.getActive());
        }

        return mapProductToProductDto(product);
    }

    @Transactional
    public void deactivate(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(()->new ProductNotFoundException("Product not found with id: "+ id));

        product.setActive(false);
    }

    private ProductDto mapProductToProductDto(Product product){
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .active(product.getActive())
                .build();
    }
}
