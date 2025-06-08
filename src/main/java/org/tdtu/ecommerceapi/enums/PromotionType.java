package org.tdtu.ecommerceapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PromotionType {
    ALL_PRODUCTS("all_products"),
    ORDER_TOTAL("order_total"),
    SPECIFIC_PRODUCTS("specific_products");

    private final String type;
}
