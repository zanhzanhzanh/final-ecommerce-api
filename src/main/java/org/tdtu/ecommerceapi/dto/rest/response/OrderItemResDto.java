package org.tdtu.ecommerceapi.dto.rest.response;

import lombok.Data;
import org.tdtu.ecommerceapi.dto.BaseDTO;
import org.tdtu.ecommerceapi.model.rest.Order;

import java.util.UUID;

@Data
public class OrderItemResDto implements BaseDTO {
    private UUID id;
    private double totalPriceProduct;
    private double updatePriceProduct;
    private int quantity;
//    private Order order;
    private ProductResDto product;
}
