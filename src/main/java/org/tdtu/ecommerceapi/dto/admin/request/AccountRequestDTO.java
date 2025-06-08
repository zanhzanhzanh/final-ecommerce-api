package org.tdtu.ecommerceapi.dto.admin.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.tdtu.ecommerceapi.dto.BaseDTO;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class AccountRequestDTO implements BaseDTO {

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

//    @NotBlank
    private String password;

    private Integer birthYear;

    @Pattern(regexp = "^0\\d{9}$", message = "Phone number must start with 0 and be 10 digits long")
    private String phoneNumber;

    private UUID groupId;
}
