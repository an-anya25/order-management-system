package com.oms.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class OrderDto {

    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal priceAtPurchase;
    private UserDto user;
}
