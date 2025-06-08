package org.tdtu.ecommerceapi.dto.rest.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.modelmapper.ModelMapper;
import org.tdtu.ecommerceapi.dto.BaseDTO;
import org.tdtu.ecommerceapi.dto.rest.request.ProductReqDto;
import org.tdtu.ecommerceapi.model.rest.Product;
import org.tdtu.ecommerceapi.utils.MappingUtils;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductResDto extends ProductReqDto implements BaseDTO {
    private UUID id;

    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        mapper.typeMap(Product.class, ProductResDto.class).addMappings(m ->
                m.map(src -> src.getCategory().getId(), ProductResDto::setCategoryId)
        );
        return mapper;
    }
}