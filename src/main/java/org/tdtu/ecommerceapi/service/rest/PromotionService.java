package org.tdtu.ecommerceapi.service.rest;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import org.tdtu.ecommerceapi.dto.rest.request.PromotionReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.PromotionResDto;
import org.tdtu.ecommerceapi.exception.ConflictException;
import org.tdtu.ecommerceapi.exception.NotFoundException;
import org.tdtu.ecommerceapi.model.rest.Product;
import org.tdtu.ecommerceapi.model.rest.Promotion;
import org.tdtu.ecommerceapi.repository.PromotionRepository;
import org.tdtu.ecommerceapi.service.BaseService;
import org.tdtu.ecommerceapi.utils.MappingUtils;

@Service
public class PromotionService extends BaseService<Promotion, PromotionReqDto, PromotionResDto, PromotionRepository> {
    private final Validator validator;
    private final ProductService productService;

    public PromotionService(
            PromotionRepository repository,
            ProductService productService,
            MappingUtils mappingUtils,
            Validator validator) {
        super(repository, mappingUtils);
        this.productService = productService;
        this.validator = validator;
    }

    @Override
    protected void postprocessModel(Promotion model, PromotionReqDto requestDTO) {
        if (repository.findByPromotionCode(requestDTO.getPromotionCode()).isPresent()) {
            throw new ConflictException(Promotion.class, "promotionCode", requestDTO.getPromotionCode());
        }

        if (requestDTO.getProductIds() != null) {
            requestDTO.getProductIds().stream()
                    .map(productId -> {
                        Product product = productService.find(productId, false);
                        if (product == null) {
                            throw new NotFoundException(Product.class, "id", productId.toString());
                        }
                        return product;
                    }).forEach(product -> {});
        }
    }

    @Override
    protected void postprocessUpdateModel(Promotion model, PromotionReqDto requestDTO) {
        if (repository.findAllByPromotionCode(requestDTO.getPromotionCode()).stream()
                .anyMatch(existingPromotion -> !existingPromotion.getId().equals(model.getId()))) {
            throw new ConflictException(Promotion.class, "promotionCode", requestDTO.getPromotionCode());
        }

        if (requestDTO.getProductIds() != null) {
            requestDTO.getProductIds().stream()
                    .map(productId -> {
                        Product product = productService.find(productId, false);
                        if (product == null) {
                            throw new NotFoundException(Product.class, "id", productId.toString());
                        }
                        return product;
                    }).forEach(product -> {
                    });
            model.setProductIds(requestDTO.getProductIds());
        }

        PromotionReqDto invalidateModel = mappingUtils.mapToDTO(model, PromotionReqDto.class);
        var violations = validator.validate(invalidateModel);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public PromotionResDto getByCode(String code) {
        return mappingUtils.mapToDTO(
                repository.findByPromotionCode(code)
                        .orElseThrow(() -> new NotFoundException(Promotion.class, "code", code))
                , PromotionResDto.class);
    }
}