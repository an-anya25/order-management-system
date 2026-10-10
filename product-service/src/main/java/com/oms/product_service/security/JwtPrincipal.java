package com.oms.product_service.security;

public record JwtPrincipal(Long userId, String email, String role) {
}
