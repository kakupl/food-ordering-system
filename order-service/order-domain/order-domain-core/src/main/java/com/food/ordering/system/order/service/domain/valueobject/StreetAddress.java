package com.food.ordering.system.order.service.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public class StreetAddress {

    private final UUID id;
    private final String street;
    private final String postalCode;
    private final String city;

    public StreetAddress(UUID id, String street, String postalCode, String city) {
        this.id = id;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
    }


    UUID getId() {
        return id;
    }

    String getStreet() {
        return street;
    }

    String getPostalCode() {
        return postalCode;
    }

    String getCity() {
        return city;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final StreetAddress that = (StreetAddress) o;
        return street.equals(that.street) &&
                postalCode.equals(that.postalCode) &&
                city.equals(that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, postalCode, city);
    }
}
