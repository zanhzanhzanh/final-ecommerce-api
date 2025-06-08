package org.tdtu.ecommerceapi.service.rest;

import org.springframework.stereotype.Service;
import org.tdtu.ecommerceapi.dto.rest.request.CartItemReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.CartItemResDto;
import org.tdtu.ecommerceapi.model.rest.CartItem;
import org.tdtu.ecommerceapi.repository.CartItemRepository;
import org.tdtu.ecommerceapi.service.BaseService;
import org.tdtu.ecommerceapi.utils.MappingUtils;

@Service
public class CartItemService extends BaseService<CartItem, CartItemReqDto, CartItemResDto, CartItemRepository> {
    public CartItemService(CartItemRepository repository, MappingUtils mappingUtils) {
        super(repository, mappingUtils);
    }
}
