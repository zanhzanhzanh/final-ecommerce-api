package org.tdtu.ecommerceapi.model.rest;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.tdtu.ecommerceapi.model.BaseModel;

@Document(collection = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem extends BaseModel {
    @PositiveOrZero(message = "productPrice must be zero or a positive number")
    private double productPrice;

    @PositiveOrZero(message = "Quantity must be zero or a positive number")
    private int quantity;

    @DocumentReference(lazy = true)
    private Cart cart;

    @DocumentReference(lazy = true)
    private Product product;
}
