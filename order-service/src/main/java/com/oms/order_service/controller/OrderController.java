package com.oms.order_service.controller;


import com.oms.order_service.dto.CreateOrderDto;
import com.oms.order_service.dto.OrderDto;
import com.oms.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderDto createOrderDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(createOrderDto));
    }
}
