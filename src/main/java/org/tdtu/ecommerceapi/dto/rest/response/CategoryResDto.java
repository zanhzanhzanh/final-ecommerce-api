package org.tdtu.ecommerceapi.dto.rest.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.tdtu.ecommerceapi.dto.BaseDTO;
import org.tdtu.ecommerceapi.dto.rest.request.CategoryReqDto;
import org.tdtu.ecommerceapi.model.rest.Category;
import org.tdtu.ecommerceapi.utils.MappingUtils;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class CategoryResDto extends CategoryReqDto implements BaseDTO {
    private UUID id;

    private List<ProductResDto> products;

    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        mapper.addMappings(groupMap(utils));
        return mapper;
    }

    public PropertyMap<Category, CategoryResDto> groupMap(MappingUtils utils) {
        return new PropertyMap<>() {
            @Override
            protected void configure() {

                Converter<Category, List<ProductResDto>> mapProducts =
                        new AbstractConverter<>() {
                            @Override
                            protected List<ProductResDto> convert(Category category) {
                                return utils.mapListToDTO(category.getProducts(), ProductResDto.class);
                            }
                        };

                using(mapProducts).map(source, destination.getProducts());
            }
        };
    }
}