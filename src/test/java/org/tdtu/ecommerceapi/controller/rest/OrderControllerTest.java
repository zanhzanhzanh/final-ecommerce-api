package org.tdtu.ecommerceapi.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.tdtu.ecommerceapi.dto.api.ApiPageableResponse;
import org.tdtu.ecommerceapi.dto.rest.request.OrderReqDto;
import org.tdtu.ecommerceapi.dto.rest.request.PlaceOrderReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.OrderResDto;
import org.tdtu.ecommerceapi.enums.DeliveryStatus;
import org.tdtu.ecommerceapi.enums.PaymentStatus;
import org.tdtu.ecommerceapi.model.rest.Order;
import org.tdtu.ecommerceapi.service.rest.OrderService;

@ContextConfiguration(classes = {OrderController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class OrderControllerTest extends BaseTest<
        OrderController,
        OrderService,
        Order,
        OrderReqDto,
        OrderResDto> {
    @Autowired
    private OrderController orderController;

    @MockitoBean
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        String endpoint = "/v1/orders";
        setupController(
                orderController,
                orderService,
                endpoint);
    }

    /**
     * Method under test: {@link OrderController#placeOrder(PlaceOrderReqDto)}
     */
    @Test
    void testPlaceOrder() throws Exception {
        // Arrange
        when(orderService.placeOrder(Mockito.<PlaceOrderReqDto>any())).thenReturn(new OrderResDto());

        PlaceOrderReqDto placeOrderReqDto = new PlaceOrderReqDto();
        placeOrderReqDto.setAddressId(UUID.randomUUID());
        placeOrderReqDto.setCartId(UUID.randomUUID());
        placeOrderReqDto.setPromotionIds(new ArrayList<>());
        placeOrderReqDto.setShipCOD(true);
        String content = (new ObjectMapper()).writeValueAsString(placeOrderReqDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"totalPrice\":0.0,\"paymentStatus\":null,\"deliveryStatus\":null,\"orderDate\":null,\"accountId\""
                                        + ":null,\"address\":null,\"usedPromotions\":null,\"orderItems\":null,\"shipCOD\":false}"));
    }

    /**
     * Method under test:
     * {@link OrderController#calTempTotalPrice(PlaceOrderReqDto)}
     */
    @Test
    void testCalTempTotalPrice() throws Exception {
        // Arrange
        when(orderService.calTempTotalPrice(Mockito.<PlaceOrderReqDto>any())).thenReturn(10.0d);

        PlaceOrderReqDto placeOrderReqDto = new PlaceOrderReqDto();
        placeOrderReqDto.setAddressId(UUID.randomUUID());
        placeOrderReqDto.setCartId(UUID.randomUUID());
        placeOrderReqDto.setPromotionIds(new ArrayList<>());
        placeOrderReqDto.setShipCOD(true);
        String content = (new ObjectMapper()).writeValueAsString(placeOrderReqDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/orders/cal-temp-total-price")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"tempTotalPrice\":10.0}"));
    }

    /**
     * Method under test: {@link OrderController#getById(UUID)}
     */
    @Test
    void testGetByIdMethod() throws Exception {
        testGetById();
    }

    /**
     * Method under test: {@link OrderController#getList(Pageable, String[])}
     */
    @Test
    void testGetListMethod() throws Exception {
        testGetList();
    }

    /**
     * Method under test: {@link OrderController#getByAccountId(UUID)}
     */
    @Test
    void testGetByAccountId() throws Exception {
        // Arrange
        when(orderService.findByAccount(Mockito.<UUID>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/orders/get-by-account/{accountId}",
                UUID.randomUUID());

        // Act and Assert
        MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link OrderController#getByAccountId(UUID)}
     */
    @Test
    void testGetByAccountId2() throws Exception {
        // Arrange
        when(orderService.findByAccount(Mockito.<UUID>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/orders/get-by-account/{accountId}",
                UUID.randomUUID());
        requestBuilder.contentType("https://example.org/example");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test:
     * {@link OrderController#takeAvailableCoupon(JwtAuthenticationToken, List)}
     */
    @Test
    void testTakeAvailableCoupon() throws Exception {
        // Arrange
        UUID accountId = UUID.randomUUID();
        List<UUID> promotionIds = List.of(UUID.randomUUID(), UUID.randomUUID());
        JwtAuthenticationToken jwtToken = mock(JwtAuthenticationToken.class);
        when(jwtToken.getToken()).thenReturn(mock(org.springframework.security.oauth2.jwt.Jwt.class));
        when(jwtToken.getToken().getClaimAsString("sub")).thenReturn(accountId.toString());
        when(orderService.getAvailablePromotions(accountId, promotionIds)).thenReturn(promotionIds);

        String content = (new ObjectMapper()).writeValueAsString(promotionIds);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/orders/take-available-coupon")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder.principal(jwtToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[\"" + promotionIds.get(0) + "\",\"" + promotionIds.get(1) + "\"]"));
    }

    /**
     * Method under test:
     * {@link OrderController#updateOrderStatus(UUID, PaymentStatus, DeliveryStatus)}
     */
    @Test
    void testUpdateOrderStatus() throws Exception {
        // Arrange
        when(orderService.updateOrderStatus(Mockito.<UUID>any(), Mockito.<PaymentStatus>any(),
                Mockito.<DeliveryStatus>any())).thenReturn(new OrderResDto());
        MockHttpServletRequestBuilder putResult = MockMvcRequestBuilders.put("/v1/orders/{id}", UUID.randomUUID());
        MockHttpServletRequestBuilder paramResult = putResult.param("deliveryStatus",
                String.valueOf(DeliveryStatus.PENDING));
        MockHttpServletRequestBuilder requestBuilder = paramResult.param("paymentStatus",
                String.valueOf(PaymentStatus.PENDING));

        // Act and Assert
        MockMvcBuilders.standaloneSetup(orderController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string(
                                "{\"id\":null,\"totalPrice\":0.0,\"paymentStatus\":null,\"deliveryStatus\":null,\"orderDate\":null,\"accountId\""
                                        + ":null,\"address\":null,\"usedPromotions\":null,\"orderItems\":null,\"shipCOD\":false}"));
    }
}
