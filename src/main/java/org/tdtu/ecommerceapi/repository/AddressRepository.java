package org.tdtu.ecommerceapi.repository;

import org.tdtu.ecommerceapi.model.rest.Address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepository extends BaseRepository<Address, UUID> {
    List<Address> findByAccount(UUID accountId);
}
