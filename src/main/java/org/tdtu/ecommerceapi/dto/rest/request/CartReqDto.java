package org.tdtu.ecommerceapi.dto.rest.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.tdtu.ecommerceapi.dto.BaseDTO;

@Data
public class CartReqDto implements BaseDTO {
    @NotNull
    private String accountId;
}
