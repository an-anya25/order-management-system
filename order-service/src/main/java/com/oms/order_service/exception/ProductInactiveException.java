package com.oms.order_service.exception;

public class ProductInactiveException extends RuntimeException{
    public ProductInactiveException(String message){
        super(message);
    }
}
