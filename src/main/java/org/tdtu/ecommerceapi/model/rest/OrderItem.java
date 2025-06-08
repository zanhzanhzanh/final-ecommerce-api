package org.tdtu.ecommerceapi.model.rest;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.tdtu.ecommerceapi.model.BaseModel;

@Document(collection = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseModel {
    @PositiveOrZero
    private double totalPriceProduct;

    @PositiveOrZero
    private double updatePriceProduct;

    @PositiveOrZero
    private int quantity;

    @DocumentReference(lazy = true)
    private Order order;

    @DocumentReference(lazy = true)
    private Product product;
}
