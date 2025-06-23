package org.tdtu.ecommerceapi.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import org.tdtu.ecommerceapi.dto.api.ApiPageableResponse;
import org.tdtu.ecommerceapi.dto.rest.request.PromotionReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.PromotionResDto;
import org.tdtu.ecommerceapi.enums.PromotionType;
import org.tdtu.ecommerceapi.enums.ProportionType;
import org.tdtu.ecommerceapi.model.rest.Promotion;
import org.tdtu.ecommerceapi.repository.CategoryRepository;
import org.tdtu.ecommerceapi.repository.ProductRepository;
import org.tdtu.ecommerceapi.repository.PromotionRepository;
import org.tdtu.ecommerceapi.service.rest.CategoryService;
import org.tdtu.ecommerceapi.service.rest.ProductService;
import org.tdtu.ecommerceapi.service.rest.PromotionService;
import org.tdtu.ecommerceapi.utils.MappingUtils;

@ContextConfiguration(classes = {PromotionController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class PromotionControllerTest extends BaseTest<
        PromotionController,
        PromotionService,
        Promotion,
        PromotionReqDto,
        PromotionResDto> {
    @Autowired
    private PromotionController promotionController;

    @MockitoBean
    private PromotionService promotionService;

    @BeforeEach
    void setUp() {
        String endpoint = "/v1/promotions";
        setupController(
                promotionController,
                promotionService,
                endpoint);
    }

    /**
     * Method under test: {@link PromotionController#getById(UUID)}
     */
    @Test
    void testGetByIdMethod() throws Exception {
        testGetById();
    }

    /**
     * Method under test: {@link PromotionController#getByCode(String)}
     */
    @Test
    void testGetByCode() throws Exception {
        // Arrange
        when(promotionService.getByCode(Mockito.<String>any())).thenReturn(new PromotionResDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/promotions/get-by-code/{code}",
                "Code");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(promotionController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"promotionName\":null,\"promotionCode\":null,\"description\":null,\"startDate\":null,\"endDate\":null,"
                                        + "\"discountAmount\":null,\"promotionType\":null,\"proportionType\":null,\"minOrderValue\":null,\"productIds\""
                                        + ":null,\"id\":null}"));
    }

    /**
     * Method under test: {@link PromotionController#create(PromotionReqDto)}
     */
    @Test
    void testDirectCreate() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        Promotion promotion = new Promotion();
        promotion.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        promotion.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        promotion.setDescription("The characteristics of someone or something");
        promotion.setDiscountAmount(10.0d);
        OffsetDateTime endDate = OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);
        promotion.setEndDate(endDate);
        UUID id = UUID.randomUUID();
        promotion.setId(id);
        promotion.setMinOrderValue(10.0d);
        promotion.setProductIds(new HashSet<>());
        promotion.setPromotionCode("Promotion Code");
        promotion.setPromotionName("Promotion Name");
        promotion.setPromotionType(PromotionType.ALL_PRODUCTS);
        promotion.setProportionType(ProportionType.PERCENTAGE);
        OffsetDateTime startDate = OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);
        promotion.setStartDate(startDate);
        promotion.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        promotion.setUpdatedBy("2020-03-01");
        promotion.setVersion(1);
        PromotionRepository repository = mock(PromotionRepository.class);
        when(repository.save(Mockito.<Promotion>any())).thenReturn(promotion);
        Optional<Promotion> emptyResult = Optional.empty();
        when(repository.findByPromotionCode(Mockito.<String>any())).thenReturn(emptyResult);
        ProductRepository repository2 = mock(ProductRepository.class);
        MappingUtils mappingUtils = new MappingUtils();
        CategoryRepository repository3 = mock(CategoryRepository.class);
        ProductService productService = new ProductService(repository2, mappingUtils,
                new CategoryService(repository3, new MappingUtils()));

        MappingUtils mappingUtils2 = new MappingUtils();
        PromotionController promotionController = new PromotionController(
                new PromotionService(repository, productService, mappingUtils2, new CustomValidatorBean()));

        // Act
        ResponseEntity<?> actualCreateResult = promotionController.create(new PromotionReqDto());

        // Assert
        verify(repository).save(isA(Promotion.class));
        verify(repository).findByPromotionCode(isNull());
        HttpStatusCode statusCode = actualCreateResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        Object body = actualCreateResult.getBody();
        assertTrue(body instanceof PromotionResDto);
        assertEquals("Promotion Code", ((PromotionResDto) body).getPromotionCode());
        assertEquals("Promotion Name", ((PromotionResDto) body).getPromotionName());
        assertEquals("The characteristics of someone or something", ((PromotionResDto) body).getDescription());
        assertEquals(10.0d, ((PromotionResDto) body).getDiscountAmount().doubleValue());
        assertEquals(10.0d, ((PromotionResDto) body).getMinOrderValue().doubleValue());
        assertEquals(200, actualCreateResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertEquals(PromotionType.ALL_PRODUCTS, ((PromotionResDto) body).getPromotionType());
        assertEquals(ProportionType.PERCENTAGE, ((PromotionResDto) body).getProportionType());
        assertTrue(((PromotionResDto) body).getProductIds().isEmpty());
        assertTrue(actualCreateResult.hasBody());
        assertTrue(actualCreateResult.getHeaders().isEmpty());
        assertSame(endDate, ((PromotionResDto) body).getEndDate());
        assertSame(startDate, ((PromotionResDto) body).getStartDate());
        assertSame(id, ((PromotionResDto) body).getId());
    }

    /**
     * Method under test: {@link PromotionController#create(PromotionReqDto)}
     */
    @Test
    void testDirectCreate2() {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

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
        PromotionRepository repository = mock(PromotionRepository.class);
        when(repository.save(Mockito.<Promotion>any())).thenReturn(promotion);
        Optional<Promotion> emptyResult = Optional.empty();
        when(repository.findByPromotionCode(Mockito.<String>any())).thenReturn(emptyResult);

        Promotion promotion2 = new Promotion();
        promotion2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        promotion2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        promotion2.setDescription("The characteristics of someone or something");
        promotion2.setDiscountAmount(10.0d);
        promotion2.setEndDate(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        promotion2.setId(UUID.randomUUID());
        promotion2.setMinOrderValue(10.0d);
        promotion2.setProductIds(new HashSet<>());
        promotion2.setPromotionCode("Promotion Code");
        promotion2.setPromotionName("Promotion Name");
        promotion2.setPromotionType(PromotionType.ALL_PRODUCTS);
        promotion2.setProportionType(ProportionType.PERCENTAGE);
        promotion2.setStartDate(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        promotion2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        promotion2.setUpdatedBy("2020-03-01");
        promotion2.setVersion(1);
        MappingUtils mappingUtils = mock(MappingUtils.class);
        PromotionResDto promotionResDto = new PromotionResDto();
        when(mappingUtils.mapToDTO(Mockito.<Promotion>any(), Mockito.<Class<PromotionResDto>>any()))
                .thenReturn(promotionResDto);
        when(mappingUtils.mapFromDTO(Mockito.<PromotionReqDto>any(), Mockito.<Class<Promotion>>any()))
                .thenReturn(promotion2);
        ProductRepository repository2 = mock(ProductRepository.class);
        MappingUtils mappingUtils2 = new MappingUtils();
        CategoryRepository repository3 = mock(CategoryRepository.class);
        ProductService productService = new ProductService(repository2, mappingUtils2,
                new CategoryService(repository3, new MappingUtils()));

        PromotionController promotionController = new PromotionController(
                new PromotionService(repository, productService, mappingUtils, new CustomValidatorBean()));

        // Act
        ResponseEntity<?> actualCreateResult = promotionController.create(new PromotionReqDto());

        // Assert
        verify(repository).save(isA(Promotion.class));
        verify(repository).findByPromotionCode(isNull());
        verify(mappingUtils).mapFromDTO(isA(PromotionReqDto.class), isA(Class.class));
        verify(mappingUtils).mapToDTO(isA(Promotion.class), isA(Class.class));
        HttpStatusCode statusCode = actualCreateResult.getStatusCode();
        assertTrue(statusCode instanceof HttpStatus);
        assertEquals(200, actualCreateResult.getStatusCodeValue());
        assertEquals(HttpStatus.OK, statusCode);
        assertTrue(actualCreateResult.hasBody());
        assertTrue(actualCreateResult.getHeaders().isEmpty());
        assertSame(promotionResDto, actualCreateResult.getBody());
    }

    /**
     * Method under test:
     * {@link PromotionController#updateById(UUID, PromotionReqDto)}
     */
    @Test
    void testUpdateByIdMethod() throws Exception {
        testUpdateById();
    }

    /**
     * Method under test: {@link PromotionController#deleteById(UUID)}
     */
    @Test
    void testDeleteByIdMethod() throws Exception {
        testDeleteById();
    }

    /**
     * Method under test: {@link PromotionController#deleteById(UUID)}
     */
    @Test
    void testDeleteByIdWithInvalidContentTypeMethod() throws Exception {
        testDeleteByIdWithInvalidContentType();
    }

    /**
     * Method under test: {@link PromotionController#getList(Pageable, String[])}
     */
    @Test
    void testGetListMethod() throws Exception {
        testGetList();
    }
}
