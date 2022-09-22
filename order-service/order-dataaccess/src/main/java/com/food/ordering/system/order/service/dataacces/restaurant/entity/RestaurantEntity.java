package com.food.ordering.system.order.service.dataacces.restaurant.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(RestaurantEntityId.class)
@Table(name = "order_restaurant", schema = "restaurant")
@Entity
public class RestaurantEntity {

    @Id
    private UUID id;
    @Id
    private UUID productId;
    private String restaurantName;
    private String restaurantActive;
    private String productName;
    private String productPrice;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RestaurantEntity that = (RestaurantEntity) o;
        return id.equals(that.id) && productId.equals(that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId);
    }
}
