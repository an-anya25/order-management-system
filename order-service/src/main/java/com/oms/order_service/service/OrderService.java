package com.oms.order_service.service;

import com.oms.order_service.client.ProductClient;
import com.oms.order_service.dto.CreateOrderDto;
import com.oms.order_service.dto.OrderDto;
import com.oms.order_service.dto.ProductDto;
import com.oms.order_service.dto.UserDto;
import com.oms.order_service.entity.Order;
import com.oms.order_service.exception.OrderAccessDeniedException;
import com.oms.order_service.exception.OrderNotFoundException;
import com.oms.order_service.repository.OrderRepository;
import com.oms.order_service.security.JwtPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@AllArgsConstructor
public class OrderService {

    private final HttpServletRequest httpServletRequest;
    private final ProductClient productClient;
    private final OrderRepository orderRepository;

    private JwtPrincipal getLoggedInPrincipal(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof JwtPrincipal jwtPrincipal){
            return jwtPrincipal;
        }

        throw new OrderAccessDeniedException("Authentication Required");
    }

    private String getBearerToken(){
        String header = httpServletRequest.getHeader("Authorization");

        if(header != null && header.startsWith("Bearer ")){
            return header.substring(7);
        }

        throw new OrderAccessDeniedException("Missing authorization token");
    }

    @Transactional
    public OrderDto createOrder(@Valid CreateOrderDto createOrderDto) {

        JwtPrincipal principal = getLoggedInPrincipal();
        ProductDto product = productClient.getProduct(createOrderDto.getProductId(), getBearerToken());

        Order order = Order.builder()
                .userId(principal.userId())
                .userName(principal.name())
                .userEmail(principal.email())
                .productId(product.getId())
                .productName(product.getName())
                .priceAtPurchase(product.getPrice())
                .build();

        Order saved = orderRepository.save(order);
        return mapOrderToOrderDto(saved);
    }

    private OrderDto mapOrderToOrderDto(Order order){
        return OrderDto.builder()
                .id(order.getId())
                .productId(order.getProductId())
                .productName(order.getProductName())
                .priceAtPurchase(order.getPriceAtPurchase())
                .user(new UserDto(order.getUserId(), order.getUserName(),order.getUserEmail()))
                .build();

    }

    public List<OrderDto> getMyOrders() {
        JwtPrincipal principal = getLoggedInPrincipal();
        return orderRepository.findByUserId(principal.userId()).stream()
                .map(this::mapOrderToOrderDto)
                .toList();
    }

    public OrderDto getMyOrder(Long id) {
        JwtPrincipal principal = getLoggedInPrincipal();
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new OrderNotFoundException("Order not found with id: "+id));

        if (!Objects.equals(order.getUserId(),principal.userId())){
            throw new OrderAccessDeniedException("You do not have access to this order");
        }

        return mapOrderToOrderDto(order);
    }

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapOrderToOrderDto)
                .toList();
    }

    public List<OrderDto> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(this::mapOrderToOrderDto)
                .toList();
    }

    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(()->new OrderNotFoundException("Order not found with id: "+id));

        orderRepository.delete(order);
    }
}
