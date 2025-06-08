package org.tdtu.ecommerceapi.dto.admin.response.extend;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.tdtu.ecommerceapi.dto.admin.response.GroupResponseDTO;
import org.tdtu.ecommerceapi.dto.admin.response.AccountResponseDTO;

@Getter
@Setter
@NoArgsConstructor
public class ExtAccountResponseDTO extends AccountResponseDTO {
    private GroupResponseDTO group;
}
