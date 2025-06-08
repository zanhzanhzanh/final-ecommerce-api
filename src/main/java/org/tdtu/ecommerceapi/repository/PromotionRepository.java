package org.tdtu.ecommerceapi.repository;

import org.tdtu.ecommerceapi.model.rest.Promotion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PromotionRepository extends BaseRepository<Promotion, UUID> {
    Optional<Promotion> findByPromotionCode(String code);
    List<Promotion> findAllByPromotionCode(String code);
}
