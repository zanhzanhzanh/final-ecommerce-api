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
public class GroupResponseDTO implements BaseDTO, Serializable {

    private UUID id;
    private String name;
}
