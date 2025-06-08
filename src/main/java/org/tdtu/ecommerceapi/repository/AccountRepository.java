package org.tdtu.ecommerceapi.repository;

import org.tdtu.ecommerceapi.model.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends BaseRepository<Account, UUID> {
    Optional<Account> findByUsername(String username);

    Optional<Account> findByEmail(String email);

    Optional<Account> findByPhoneNumber(String phoneNumber);

    Optional<Account> findByEmailAndIdNot(String email, UUID id);

    Optional<Account> findByPhoneNumberAndIdNot(String phoneNumber, UUID id);
}
