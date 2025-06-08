package org.tdtu.ecommerceapi.model.rest;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.tdtu.ecommerceapi.model.Account;
import org.tdtu.ecommerceapi.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart extends BaseModel {

    @DocumentReference(lazy = true)
    private Account account;

    @DocumentReference(lazy = true, lookup = "{ 'cart' : ?#{#self._id} }")
    @ReadOnlyProperty
    private List<CartItem> cartItems = new ArrayList<>();
}
