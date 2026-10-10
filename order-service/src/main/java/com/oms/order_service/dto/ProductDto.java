package com.oms.order_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private Boolean active;
}
