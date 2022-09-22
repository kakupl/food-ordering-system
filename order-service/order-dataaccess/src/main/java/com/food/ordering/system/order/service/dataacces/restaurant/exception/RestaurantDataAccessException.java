package com.food.ordering.system.order.service.dataacces.restaurant.exception;

import com.food.ordering.system.domain.exception.DomainException;

public class RestaurantDataAccessException extends RuntimeException {

    public RestaurantDataAccessException( String message) {
        super(message);
    }
}
