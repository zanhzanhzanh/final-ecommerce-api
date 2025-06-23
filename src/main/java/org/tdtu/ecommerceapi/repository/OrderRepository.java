package org.tdtu.ecommerceapi.repository;

import org.tdtu.ecommerceapi.model.rest.Order;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends BaseRepository<Order, UUID> {
    List<Order> findByAccount(UUID accountId);
}
