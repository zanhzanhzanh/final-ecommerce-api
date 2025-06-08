package org.tdtu.ecommerceapi.dto.external.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class StripeChargeRequestDTO {
    @NotBlank
    private String token;

    @NotNull
    @Min(0)
    private long amount;

    @NotBlank
    private String currency;
}
