package org.tdtu.ecommerceapi.dto.rest.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.tdtu.ecommerceapi.dto.BaseDTO;
import org.tdtu.ecommerceapi.dto.rest.request.PromotionReqDto;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class PromotionResDto extends PromotionReqDto implements BaseDTO {
    private UUID id;
}
