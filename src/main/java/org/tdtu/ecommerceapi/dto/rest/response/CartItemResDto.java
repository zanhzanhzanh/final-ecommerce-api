package org.tdtu.ecommerceapi.dto.rest.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.tdtu.ecommerceapi.dto.BaseDTO;
import org.tdtu.ecommerceapi.dto.rest.request.CartItemReqDto;
import org.tdtu.ecommerceapi.model.rest.CartItem;
import org.tdtu.ecommerceapi.utils.MappingUtils;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class CartItemResDto extends CartItemReqDto implements BaseDTO {
    private UUID id;

    private ProductResDto product;

    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        mapper.typeMap(CartItem.class, CartItemResDto.class).addMappings(m ->
                m.map(src -> src.getCart().getId(), CartItemResDto::setCartId)
        );

//        mapper.addMappings(new PropertyMap<CartItem, CartItemResDto>() {
//            @Override
//            protected void configure() {
//                using((Converter<CartItem, ProductResDto>) context ->
//                        utils.mapToDTO(context.getSource().getProduct(), ProductResDto.class)
//                ).map(source, destination.getProduct());
//            }
//        });
//        mapper.addMappings(groupMap(utils));
        return mapper;
    }

//    public PropertyMap<CartItem, CartItemResDto> groupMap(MappingUtils utils) {
//        return new PropertyMap<>() {
//            @Override
//            protected void configure() {
//
//                Converter<CartItem, ProductResDto> mapProducts =
//                        new AbstractConverter<>() {
//                            @Override
//                            protected ProductResDto convert(CartItem context) {
//                                return utils.mapToDTO(context.getProduct(), ProductResDto.class);
//                            }
//                        };
//
//                using(mapProducts).map(source, destination.getProduct());
//            }
//        };
//    }
}