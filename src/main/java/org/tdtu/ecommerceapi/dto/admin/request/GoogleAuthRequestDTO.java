package org.tdtu.ecommerceapi.dto.admin.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.tdtu.ecommerceapi.dto.BaseDTO;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class GoogleAuthRequestDTO implements BaseDTO {
    @NotBlank
    private String idToken;
}
