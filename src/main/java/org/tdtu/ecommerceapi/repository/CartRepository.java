package org.tdtu.ecommerceapi.repository;

import org.tdtu.ecommerceapi.model.rest.Cart;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends BaseRepository<Cart, UUID> {
    List<Cart> findAllByAccountId(UUID accountId);
}
