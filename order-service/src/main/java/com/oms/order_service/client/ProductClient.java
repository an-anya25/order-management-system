package com.oms.order_service.client;

import com.oms.order_service.dto.ProductDto;
import com.oms.order_service.exception.ProductInactiveException;
import com.oms.order_service.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductClient {

    private final RestClient productRestClient;

    public ProductDto getProduct(Long productId, String bearerToken){
        try {

            ProductDto product = productRestClient.get()
                    .uri("/api/v1/products/{id}",productId)
                    .header("Authorization", "Bearer "+bearerToken)
                    .retrieve()
                    .body(ProductDto.class);

            if(product == null){
                log.error("null product returned");
                throw new ProductNotFoundException("Product not found with id: "+ productId);
            }

            if(!product.getActive()){
                log.error("product inactive");
                throw new ProductInactiveException("Product is not available for ordering");
            }

            return product;

        } catch (HttpClientErrorException.NotFound ex){
            log.error("not found");
            throw new ProductNotFoundException("Product not found with id: "+ productId);
        } catch (HttpClientErrorException.Forbidden | HttpClientErrorException.Unauthorized ex){
            log.error("forbidden/unauthorised");
            throw new ProductNotFoundException("Unable to access product catalog");
        }
    }
}
