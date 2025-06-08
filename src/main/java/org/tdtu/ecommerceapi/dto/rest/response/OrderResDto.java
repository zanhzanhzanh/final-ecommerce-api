package org.tdtu.ecommerceapi.dto.rest.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.tdtu.ecommerceapi.dto.BaseDTO;
import org.tdtu.ecommerceapi.dto.rest.request.OrderReqDto;
import org.tdtu.ecommerceapi.enums.DeliveryStatus;
import org.tdtu.ecommerceapi.enums.PaymentStatus;
import org.tdtu.ecommerceapi.model.Account;
import org.tdtu.ecommerceapi.model.rest.*;
import org.tdtu.ecommerceapi.utils.MappingUtils;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class OrderResDto implements BaseDTO {
    private UUID id;
    private double totalPrice;
    private PaymentStatus paymentStatus;
    private DeliveryStatus deliveryStatus;
    private boolean isShipCOD;
    private OffsetDateTime orderDate;

    private UUID accountId;
    private AddressResDto address;

    private List<Promotion> usedPromotions;
    private List<OrderItemResDto> orderItems;

    @Override
    public ModelMapper updateModelMapper(ModelMapper mapper, MappingUtils utils) {
        mapper.typeMap(Order.class, OrderResDto.class).addMappings(m ->
                m.map(src -> src.getAccount().getId(), OrderResDto::setAccountId)
        );
//        mapper.addMappings(groupMap(utils));
        return mapper;
    }
//
//    public PropertyMap<Order, OrderResDto> groupMap(MappingUtils utils) {
//        return new PropertyMap<>() {
//            @Override
//            protected void configure() {
//
//                Converter<Order, List<OrderItemResDto>> mapProducts =
//                        new AbstractConverter<>() {
//                            @Override
//                            protected List<OrderItemResDto> convert(Order context) {
//                                return utils.mapListToDTO(context.getOrderItems(), OrderItemResDto.class);
//                            }
//                        };
//
//                using(mapProducts).map(source, destination.getOrderItems());
//            }
//        };
//    }
}
