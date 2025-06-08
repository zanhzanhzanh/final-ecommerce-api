package org.tdtu.ecommerceapi.dto.rest.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.tdtu.ecommerceapi.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class PlaceOrderReqDto implements BaseDTO {
    @NotNull private boolean isShipCOD;
    @NotNull private UUID cartId;
    @NotNull private UUID addressId;
    private List<UUID> promotionIds = new ArrayList<>();
}
