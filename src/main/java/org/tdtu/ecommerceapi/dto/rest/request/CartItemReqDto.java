package org.tdtu.ecommerceapi.dto.rest.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.tdtu.ecommerceapi.dto.BaseDTO;

import java.util.UUID;

@Data
public class CartItemReqDto implements BaseDTO {
    @PositiveOrZero(message = "productPrice must be zero or a positive number")
    private double productPrice;

    @PositiveOrZero(message = "Quantity must be zero or a positive number")
    private int quantity;

    @NotNull
    private UUID cartId;

//    @NotNull
//    private UUID productId;
}
