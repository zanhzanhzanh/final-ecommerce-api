package org.tdtu.ecommerceapi.repository;

import org.tdtu.ecommerceapi.model.AppGroup;

import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends BaseRepository<AppGroup, UUID> {
    Optional<AppGroup> findByName(String name);
}
