package com.oms.order_service.security;

public record JwtPrincipal(Long userId, String email, String role, String name) {
}
