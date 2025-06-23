package org.tdtu.ecommerceapi.service.rest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tdtu.ecommerceapi.dto.BaseDTO;
import org.tdtu.ecommerceapi.dto.admin.request.AccountRequestDTO;
import org.tdtu.ecommerceapi.dto.rest.request.PromotionReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.PromotionResDto;
import org.tdtu.ecommerceapi.enums.PromotionType;
import org.tdtu.ecommerceapi.enums.ProportionType;
import org.tdtu.ecommerceapi.exception.ConflictException;
import org.tdtu.ecommerceapi.exception.NotFoundException;
import org.tdtu.ecommerceapi.model.rest.Promotion;
import org.tdtu.ecommerceapi.repository.PromotionRepository;
import org.tdtu.ecommerceapi.utils.MappingUtils;

@ContextConfiguration(classes = {PromotionService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class PromotionServiceTest {
    @MockitoBean
    private MappingUtils mappingUtils;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private PromotionRepository promotionRepository;

    @Autowired
    private PromotionService promotionService;

    @MockitoBean
    private Validator validator;

    /**
     * Method under test:
     * {@link PromotionService#postprocessModel(Promotion, PromotionReqDto)}
     */
    @Test
    void testPostprocessModel() {
        // Arrange
        Promotion promotion = new Promotion();
        promotion.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        promotion.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        promotion.setDescription("The characteristics of someone or something");
        promotion.setDiscountAmount(10.0d);
        promotion.setEndDate(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        promotion.setId(UUID.randomUUID());
        promotion.setMinOrderValue(10.0d);
        promotion.setProductIds(new HashSet<>());
        promotion.setPromotionCode("Promotion Code");
        promotion.setPromotionName("Promotion Name");
        promotion.setPromotionType(PromotionType.ALL_PRODUCTS);
        promotion.setProportionType(ProportionType.PERCENTAGE);
        promotion.setStartDate(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        promotion.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        promotion.setUpdatedBy("2020-03-01");
        promotion.setVersion(1);
        Optional<Promotion> ofResult = Optional.of(promotion);
        when(promotionRepository.findByPromotionCode(Mockito.<String>any())).thenReturn(ofResult);

        Promotion model = new Promotion();
        model.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        model.setDescription("The characteristics of someone or something");
        model.setDiscountAmount(10.0d);
        model.setEndDate(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setId(UUID.randomUUID());
        model.setMinOrderValue(10.0d);
        model.setProductIds(new HashSet<>());
        model.setPromotionCode("Promotion Code");
        model.setPromotionName("Promotion Name");
        model.setPromotionType(PromotionType.ALL_PRODUCTS);
        model.setProportionType(ProportionType.PERCENTAGE);
        model.setStartDate(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setUpdatedBy("2020-03-01");
        model.setVersion(1);

        // Act and Assert
        assertThrows(ConflictException.class, () -> promotionService.postprocessModel(model, new PromotionReqDto()));
        verify(promotionRepository).findByPromotionCode(isNull());
    }

    /**
     * Method under test:
     * {@link PromotionService#postprocessModel(Promotion, PromotionReqDto)}
     */
    @Test
    void testPostprocessModel2() {
        // Arrange
        Optional<Promotion> emptyResult = Optional.empty();
        when(promotionRepository.findByPromotionCode(Mockito.<String>any())).thenReturn(emptyResult);

        Promotion model = new Promotion();
        model.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        model.setDescription("The characteristics of someone or something");
        model.setDiscountAmount(10.0d);
        model.setEndDate(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setId(UUID.randomUUID());
        model.setMinOrderValue(10.0d);
        model.setProductIds(new HashSet<>());
        model.setPromotionCode("Promotion Code");
        model.setPromotionName("Promotion Name");
        model.setPromotionType(PromotionType.ALL_PRODUCTS);
        model.setProportionType(ProportionType.PERCENTAGE);
        model.setStartDate(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setUpdatedBy("2020-03-01");
        model.setVersion(1);

        // Act
        promotionService.postprocessModel(model, new PromotionReqDto());

        // Assert that nothing has changed
        verify(promotionRepository).findByPromotionCode(isNull());
    }

    /**
     * Method under test:
     * {@link PromotionService#postprocessUpdateModel(Promotion, PromotionReqDto)}
     */
    @Test
    void testPostprocessUpdateModel() {
        // Arrange
        when(promotionRepository.findAllByPromotionCode(Mockito.<String>any())).thenReturn(new ArrayList<>());
        when(mappingUtils.mapToDTO(Mockito.<Promotion>any(), Mockito.<Class<PromotionReqDto>>any()))
                .thenReturn(new PromotionReqDto());
        when(validator.validate(Mockito.<PromotionReqDto>any(), isA(Class[].class))).thenReturn(new HashSet<>());

        Promotion model = new Promotion();
        model.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        model.setDescription("The characteristics of someone or something");
        model.setDiscountAmount(10.0d);
        model.setEndDate(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setId(UUID.randomUUID());
        model.setMinOrderValue(10.0d);
        model.setProductIds(new HashSet<>());
        model.setPromotionCode("Promotion Code");
        model.setPromotionName("Promotion Name");
        model.setPromotionType(PromotionType.ALL_PRODUCTS);
        model.setProportionType(ProportionType.PERCENTAGE);
        model.setStartDate(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setUpdatedBy("2020-03-01");
        model.setVersion(1);

        // Act
        promotionService.postprocessUpdateModel(model, new PromotionReqDto());

        // Assert that nothing has changed
        verify(validator).validate(isA(PromotionReqDto.class), isA(Class[].class));
        verify(promotionRepository).findAllByPromotionCode(isNull());
        verify(mappingUtils).mapToDTO(isA(Promotion.class), isA(Class.class));
    }

    /**
     * Method under test:
     * {@link PromotionService#postprocessUpdateModel(Promotion, PromotionReqDto)}
     */
    @Test
    void testPostprocessUpdateModel2() {
        // Arrange
        when(promotionRepository.findAllByPromotionCode(Mockito.<String>any())).thenReturn(new ArrayList<>());
        when(mappingUtils.mapToDTO(Mockito.<Promotion>any(), Mockito.<Class<PromotionReqDto>>any()))
                .thenReturn(new PromotionReqDto());
        when(validator.validate(Mockito.<PromotionReqDto>any(), isA(Class[].class)))
                .thenThrow(new ConstraintViolationException(new HashSet<>()));

        Promotion model = new Promotion();
        model.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        model.setDescription("The characteristics of someone or something");
        model.setDiscountAmount(10.0d);
        model.setEndDate(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setId(UUID.randomUUID());
        model.setMinOrderValue(10.0d);
        model.setProductIds(new HashSet<>());
        model.setPromotionCode("Promotion Code");
        model.setPromotionName("Promotion Name");
        model.setPromotionType(PromotionType.ALL_PRODUCTS);
        model.setProportionType(ProportionType.PERCENTAGE);
        model.setStartDate(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setUpdatedBy("2020-03-01");
        model.setVersion(1);

        // Act and Assert
        assertThrows(ConstraintViolationException.class,
                () -> promotionService.postprocessUpdateModel(model, new PromotionReqDto()));
        verify(validator).validate(isA(PromotionReqDto.class), isA(Class[].class));
        verify(promotionRepository).findAllByPromotionCode(isNull());
        verify(mappingUtils).mapToDTO(isA(Promotion.class), isA(Class.class));
    }

    /**
     * Method under test: {@link PromotionService#getByCode(String)}
     */
    @Test
    void testGetByCode() {
        // Arrange
        Promotion promotion = new Promotion();
        promotion.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        promotion.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        promotion.setDescription("The characteristics of someone or something");
        promotion.setDiscountAmount(10.0d);
        promotion.setEndDate(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        promotion.setId(UUID.randomUUID());
        promotion.setMinOrderValue(10.0d);
        promotion.setProductIds(new HashSet<>());
        promotion.setPromotionCode("Promotion Code");
        promotion.setPromotionName("Promotion Name");
        promotion.setPromotionType(PromotionType.ALL_PRODUCTS);
        promotion.setProportionType(ProportionType.PERCENTAGE);
        promotion.setStartDate(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        promotion.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        promotion.setUpdatedBy("2020-03-01");
        promotion.setVersion(1);
        Optional<Promotion> ofResult = Optional.of(promotion);
        when(promotionRepository.findByPromotionCode(Mockito.<String>any())).thenReturn(ofResult);
        PromotionResDto promotionResDto = new PromotionResDto();
        when(mappingUtils.mapToDTO(Mockito.<Promotion>any(), Mockito.<Class<PromotionResDto>>any()))
                .thenReturn(promotionResDto);

        // Act
        PromotionResDto actualByCode = promotionService.getByCode("Code");

        // Assert
        verify(promotionRepository).findByPromotionCode(eq("Code"));
        verify(mappingUtils).mapToDTO(isA(Promotion.class), isA(Class.class));
        assertSame(promotionResDto, actualByCode);
    }

    /**
     * Method under test: {@link PromotionService#getByCode(String)}
     */
    @Test
    void testGetByCode2() {
        // Arrange
        Promotion promotion = new Promotion();
        promotion.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        promotion.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        promotion.setDescription("The characteristics of someone or something");
        promotion.setDiscountAmount(10.0d);
        promotion.setEndDate(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        promotion.setId(UUID.randomUUID());
        promotion.setMinOrderValue(10.0d);
        promotion.setProductIds(new HashSet<>());
        promotion.setPromotionCode("Promotion Code");
        promotion.setPromotionName("Promotion Name");
        promotion.setPromotionType(PromotionType.ALL_PRODUCTS);
        promotion.setProportionType(ProportionType.PERCENTAGE);
        promotion.setStartDate(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        promotion.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        promotion.setUpdatedBy("2020-03-01");
        promotion.setVersion(1);
        Optional<Promotion> ofResult = Optional.of(promotion);
        when(promotionRepository.findByPromotionCode(Mockito.<String>any())).thenReturn(ofResult);
        when(mappingUtils.mapToDTO(Mockito.<Promotion>any(), Mockito.<Class<PromotionResDto>>any()))
                .thenThrow(new ConstraintViolationException(new HashSet<>()));

        // Act and Assert
        assertThrows(ConstraintViolationException.class, () -> promotionService.getByCode("Code"));
        verify(promotionRepository).findByPromotionCode(eq("Code"));
        verify(mappingUtils).mapToDTO(isA(Promotion.class), isA(Class.class));
    }
}
