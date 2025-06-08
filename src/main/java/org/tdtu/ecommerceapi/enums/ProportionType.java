package org.tdtu.ecommerceapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProportionType {
    PERCENTAGE("percentage"),
    ABSOLUTE("absolute");

    private final String type;
}
