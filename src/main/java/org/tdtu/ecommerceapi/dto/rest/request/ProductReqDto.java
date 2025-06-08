package org.tdtu.ecommerceapi.dto.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.tdtu.ecommerceapi.dto.BaseDTO;

import java.util.UUID;

@Data
public class ProductReqDto implements BaseDTO {
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

    @NotNull
    private UUID categoryId;
}
