package org.tdtu.ecommerceapi.model.rest;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.tdtu.ecommerceapi.model.BaseModel;
import org.tdtu.ecommerceapi.model.rest.Category;

@Document(collection = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseModel {
    @NotBlank
    @Size(min = 5, message = "Product name must contain at least 5 characters")
    private String productName;

    private String image;

    @NotBlank
    @Size(min = 5, message = "Product description must contain at least 5 characters")
    private String description;

    @NotNull
    @PositiveOrZero(message = "Quantity must be zero or a positive number")
    private int quantity;

    @NotNull
    @PositiveOrZero(message = "Price must be a positive number")
    private double price;

//    @PositiveOrZero(message = "Discount must be zero or a positive number")
//    private double discount;
//
//    @PositiveOrZero(message = "Special price must be zero or a positive number")
//    private double specialPrice;

    @DocumentReference(lazy = true)
    private Category category;

//    private Account account;

//    private List<CartItem> productCartItems = new ArrayList<>();
}
