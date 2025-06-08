package org.tdtu.ecommerceapi.model.rest;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.tdtu.ecommerceapi.dto.group.OnCreate;
import org.tdtu.ecommerceapi.enums.PromotionType;
import org.tdtu.ecommerceapi.enums.ProportionType;
import org.tdtu.ecommerceapi.model.BaseModel;

import java.time.OffsetDateTime;
import java.util.*;

@Document(collection = "promotions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Promotion extends BaseModel {
    @NotBlank
    private String promotionName;

//    @NotBlank
//    @Indexed(unique = true)
    private String promotionCode;

    @NotBlank
    private String description;

    @NotNull
    private OffsetDateTime startDate;

    @NotNull
    private OffsetDateTime endDate;

    @NotNull(groups = {OnCreate.class})
    private Double discountAmount;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PromotionType promotionType;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ProportionType proportionType;

    @NotNull(groups = {OnCreate.class})
    private Double minOrderValue;

    private Set<UUID> productIds = new HashSet<>();
}
