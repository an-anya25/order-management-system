package com.oms.product_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ProductDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private Boolean active;

}
