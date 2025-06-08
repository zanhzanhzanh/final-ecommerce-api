package org.tdtu.ecommerceapi.dto.rest.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.modelmapper.ModelMapper;
import org.tdtu.ecommerceapi.dto.BaseDTO;
import org.tdtu.ecommerceapi.dto.rest.request.AddressReqDto;
import org.tdtu.ecommerceapi.model.rest.Address;
import org.tdtu.ecommerceapi.utils.MappingUtils;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class AddressResDto extends AddressReqDto implements BaseDTO {
    private UUID id;

    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        mapper.typeMap(Address.class, AddressResDto.class).addMappings(
                m -> m.map(src -> src.getAccount().getId(), AddressResDto::setAccountId)
        );
        return mapper;
    }
}
