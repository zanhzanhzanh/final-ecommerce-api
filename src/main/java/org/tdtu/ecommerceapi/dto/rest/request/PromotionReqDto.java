package org.tdtu.ecommerceapi.dto.rest.request;


import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.tdtu.ecommerceapi.dto.BaseDTO;
import org.tdtu.ecommerceapi.dto.group.OnCreate;
import org.tdtu.ecommerceapi.enums.PromotionType;
import org.tdtu.ecommerceapi.enums.ProportionType;
import org.tdtu.ecommerceapi.utils.annotation.ValidPromotion;

import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@ValidPromotion
public class PromotionReqDto implements BaseDTO {
    @NotBlank
    @Size(min = 5, message = "Promotion name must contain at least 5 characters")
    private String promotionName;

//    @NotBlank
//    @Indexed(unique = true)
    @Size(min = 5, message = "Promotion code must contain at least 5 characters")
    @Pattern(regexp = "^[A-Z0-9]+$", message = "Promotion code must be alphanumeric and uppercase")
    private String promotionCode;

    @NotBlank
    @Size(min = 10, message = "Promotion description must contain at least 10 characters")
    private String description;

    @NotNull
    @FutureOrPresent(message = "Start date must be in the present or future")
    private OffsetDateTime startDate;

    @NotNull
    @Future(message = "End date must be in the future")
    private OffsetDateTime endDate;

    @Positive(message = "Discount amount must be a positive number")
    @NotNull(groups = {OnCreate.class})
    private Double discountAmount;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PromotionType promotionType;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ProportionType proportionType;

    @PositiveOrZero(message = "Minimum order value must be zero or a positive number")
    @NotNull(groups = {OnCreate.class})
    private Double minOrderValue;

    private Set<UUID> productIds;
}
