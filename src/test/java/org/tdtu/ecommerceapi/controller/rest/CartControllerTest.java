package org.tdtu.ecommerceapi.controller.rest;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
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
import org.tdtu.ecommerceapi.dto.rest.request.CartQuantityReqDto;
import org.tdtu.ecommerceapi.dto.rest.request.CartReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.CartResDto;
import org.tdtu.ecommerceapi.model.rest.Cart;
import org.tdtu.ecommerceapi.service.rest.CartService;

@ContextConfiguration(classes = {CartController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CartControllerTest extends BaseTest<
        CartController,
        CartService,
        Cart,
        CartReqDto,
        CartResDto> {
    @Autowired
    private CartController cartController;

    @MockitoBean
    private CartService cartService;

    @BeforeEach
    void setUp() {
        String endpoint = "/v1/carts";
        setupController(
                cartController,
                cartService,
                endpoint);
    }

    /**
     * Method under test:
     * {@link CartController#updateCartItemQuantity(CartQuantityReqDto)}
     */
    @Test
    void testUpdateCartItemQuantity() throws Exception {
        // Arrange
        when(cartService.updateCartItemQuantity(Mockito.<CartQuantityReqDto>any())).thenReturn(new CartResDto());

        CartQuantityReqDto cartQuantityReqDto = new CartQuantityReqDto();
        cartQuantityReqDto.setCartId(UUID.randomUUID());
        cartQuantityReqDto.setDelta(2);
        cartQuantityReqDto.setProductId(UUID.randomUUID());
        String content = (new ObjectMapper()).writeValueAsString(cartQuantityReqDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/carts/update-item-quantity")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"accountId\":null,\"id\":null,\"cartItems\":null}"));
    }

    /**
     * Method under test: {@link CartController#refreshCart(UUID)}
     */
    @Test
    void testRefreshCart() throws Exception {
        // Arrange
        when(cartService.refreshCart(Mockito.<UUID>any())).thenReturn(new CartResDto());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/carts/refresh-cart/{cartId}",
                UUID.randomUUID());

        // Act and Assert
        MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"accountId\":null,\"id\":null,\"cartItems\":null}"));
    }

    /**
     * Method under test: {@link CartController#getById(UUID)}
     */
    @Test
    void testGetByIdMethod() throws Exception {
        testGetById();
    }

    /**
     * Method under test: {@link CartController#getByToken(JwtAuthenticationToken)}
     */
    @Test
    void testGetByToken() throws Exception {
        // Arrange
        UUID accountId = UUID.randomUUID();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", accountId.toString())
                .build();
        JwtAuthenticationToken jwtToken = new JwtAuthenticationToken(jwt);

        when(cartService.getByToken(accountId)).thenReturn(new CartResDto());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/carts/get-by-token")
                .principal(jwtToken);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"accountId\":null,\"id\":null,\"cartItems\":null}"));
    }

    /**
     * Method under test: {@link CartController#create(JwtAuthenticationToken)}
     */
    @Test
    void testCreateMethod() throws Exception {
        // Arrange
        UUID accountId = UUID.randomUUID();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", accountId.toString())
                .build();
        JwtAuthenticationToken jwtToken = new JwtAuthenticationToken(jwt);

        when(cartService.create(Mockito.any(CartReqDto.class))).thenReturn(new CartResDto());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/carts")
                .principal(jwtToken);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"accountId\":null,\"id\":null,\"cartItems\":null}"));
    }

    /**
     * Method under test:
     * {@link CartController#updateById(JwtAuthenticationToken, UUID)}
     */
    @Test
    void testUpdateByIdMethod() throws Exception {
        // Arrange
        UUID accountId = UUID.randomUUID();
        UUID cartId = UUID.randomUUID();
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "none")
                .claim("sub", accountId.toString())
                .build();
        JwtAuthenticationToken jwtToken = new JwtAuthenticationToken(jwt);

        when(cartService.updateById(Mockito.eq(cartId), Mockito.any(CartReqDto.class))).thenReturn(new CartResDto());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/v1/carts/{id}", cartId)
                .principal(jwtToken);

        // Act and Assert
        MockMvcBuilders.standaloneSetup(cartController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("{\"accountId\":null,\"id\":null,\"cartItems\":null}"));
    }

    /**
     * Method under test: {@link CartController#getList(Pageable, String[])}
     */
    @Test
    void testGetListMethod() throws Exception {
        testGetList();
    }
}
