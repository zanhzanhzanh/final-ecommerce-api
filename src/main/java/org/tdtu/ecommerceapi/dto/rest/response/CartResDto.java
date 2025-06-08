package org.tdtu.ecommerceapi.dto.rest.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.tdtu.ecommerceapi.dto.BaseDTO;
import org.tdtu.ecommerceapi.dto.rest.request.CartReqDto;
import org.tdtu.ecommerceapi.model.rest.Cart;
import org.tdtu.ecommerceapi.utils.MappingUtils;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class CartResDto extends CartReqDto implements BaseDTO {
    private UUID id;

    private List<CartItemResDto> cartItems;

    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        mapper.typeMap(Cart.class, CartResDto.class).addMappings(m ->
                m.map(src -> src.getAccount().getId(), CartResDto::setAccountId)
        );
//        mapper.addMappings(new PropertyMap<Cart, CartResDto>() {
//            @Override
//            protected void configure() {
//                using((Converter<Cart, List<CartItemResDto>>) context ->
//                        utils.mapListToDTO(context.getSource().getCartItems(), CartItemResDto.class)
//                ).map(source, destination.getCartItems());
//            }
//        });
        mapper.addMappings(groupMap(utils));
        return mapper;
    }

    public PropertyMap<Cart, CartResDto> groupMap(MappingUtils utils) {
        return new PropertyMap<>() {
            @Override
            protected void configure() {

                Converter<Cart, List<CartItemResDto>> mapProducts =
                        new AbstractConverter<>() {
                            @Override
                            protected List<CartItemResDto> convert(Cart context) {
                                return utils.mapListToDTO(context.getCartItems(), CartItemResDto.class);
                            }
                        };

                using(mapProducts).map(source, destination.getCartItems());
            }
        };
    }
}