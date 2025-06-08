package org.tdtu.ecommerceapi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SendGridTemplate {
    SIGNUP_TEMPLATE("d-c27e5561f4c54f8fb1ac06e3d1d3c7a4");

    private final String templateId;
}
