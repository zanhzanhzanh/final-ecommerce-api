package org.tdtu.ecommerceapi.utils.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.tdtu.ecommerceapi.dto.rest.request.PromotionReqDto;
import org.tdtu.ecommerceapi.enums.PromotionType;
import org.tdtu.ecommerceapi.enums.ProportionType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

@Constraint(validatedBy = ValidPromotion.PromotionReqDtoValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPromotion {
    String message() default "Invalid promotion request";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class PromotionReqDtoValidator implements ConstraintValidator<ValidPromotion, PromotionReqDto> {
        @Override
        public boolean isValid(PromotionReqDto dto, ConstraintValidatorContext context) {
            // VALID: endDate > startDate
            if (dto.getEndDate() != null && dto.getStartDate() != null && dto.getEndDate().isBefore(dto.getStartDate())) {
                context.buildConstraintViolationWithTemplate("End date must be greater than start date.")
                        .addPropertyNode("endDate").addConstraintViolation();
                return false;
            }

            // VALID: proportionType
            if (dto.getProportionType() != null && Arrays.stream(ProportionType.values())
                    .noneMatch(type -> type == dto.getProportionType())) {
                context.buildConstraintViolationWithTemplate("Proportion type is not a valid enum value.")
                        .addPropertyNode("proportionType").addConstraintViolation();
                return false;
            }

            // VALID: promotionType
            if (dto.getPromotionType() == PromotionType.ALL_PRODUCTS) {
                if (dto.getProportionType() == ProportionType.ABSOLUTE) {
                    context.buildConstraintViolationWithTemplate("Proportion type cannot be ABSOLUTE for ALL_PRODUCTS promotion type.")
                            .addPropertyNode("proportionType").addConstraintViolation();
                    return false;
                }
                if (dto.getMinOrderValue() != null && dto.getMinOrderValue() > 0) {
                    context.buildConstraintViolationWithTemplate("Can't use minOrderValue for ALL_PRODUCTS promotion type. Set minOrderValue to 0.")
                            .addPropertyNode("minOrderValue").addConstraintViolation();
                    return false;
                }
                if (dto.getProductIds() != null && !dto.getProductIds().isEmpty()) {
                    context.buildConstraintViolationWithTemplate("Product IDs must be empty for ALL_PRODUCTS promotion type.")
                            .addPropertyNode("productIds").addConstraintViolation();
                    return false;
                }
                return true;
            } else if (dto.getPromotionType() == PromotionType.ORDER_TOTAL) {
                if (dto.getProductIds() != null && !dto.getProductIds().isEmpty()) {
                    context.buildConstraintViolationWithTemplate("Product IDs must be empty for ORDER_TOTAL promotion type.")
                            .addPropertyNode("productIds").addConstraintViolation();
                    return false;
                }
                return true;
            } else if (dto.getPromotionType() == PromotionType.SPECIFIC_PRODUCTS) {
                if (dto.getMinOrderValue() != null && dto.getMinOrderValue() > 0) {
                    context.buildConstraintViolationWithTemplate("Can't use minOrderValue for SPECIFIC_PRODUCTS promotion type. Set minOrderValue to 0.")
                            .addPropertyNode("minOrderValue").addConstraintViolation();
                    return false;
                }
                return true;
            }

            context.buildConstraintViolationWithTemplate("Promotion type is either null or not a valid enum value.")
                    .addPropertyNode("promotionType").addConstraintViolation();
            return false;
        }
    }
}
