package org.tdtu.ecommerceapi.dto.admin.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tdtu.ecommerceapi.dto.BaseDTO;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class AccountResponseDTO implements BaseDTO, Serializable {
    private UUID id;
    private String username;
    private String email;
    private Integer birthYear;
    private String phoneNumber;
}
