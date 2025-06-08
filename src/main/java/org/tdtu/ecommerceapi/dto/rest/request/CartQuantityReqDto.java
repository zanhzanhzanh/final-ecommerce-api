package org.tdtu.ecommerceapi.dto.rest.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.tdtu.ecommerceapi.dto.BaseDTO;

import java.util.UUID;

@Data
public class CartQuantityReqDto implements BaseDTO {
    private UUID cartId = UUID.fromString("00000000-0000-0000-0000-000000000000");
    @NotNull private UUID productId;
    @NotNull private Integer delta;
}
