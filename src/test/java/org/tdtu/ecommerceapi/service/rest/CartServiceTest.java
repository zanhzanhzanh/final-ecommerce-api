package org.tdtu.ecommerceapi.service.rest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tdtu.ecommerceapi.dto.rest.request.CartQuantityReqDto;
import org.tdtu.ecommerceapi.dto.rest.request.CartReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.CartResDto;
import org.tdtu.ecommerceapi.exception.NotFoundException;
import org.tdtu.ecommerceapi.model.Account;
import org.tdtu.ecommerceapi.model.AppGroup;
import org.tdtu.ecommerceapi.model.GoogleAccount;
import org.tdtu.ecommerceapi.model.rest.Cart;
import org.tdtu.ecommerceapi.model.rest.CartItem;
import org.tdtu.ecommerceapi.model.rest.Product;
import org.tdtu.ecommerceapi.repository.CartItemRepository;
import org.tdtu.ecommerceapi.repository.CartRepository;
import org.tdtu.ecommerceapi.service.AccountService;
import org.tdtu.ecommerceapi.utils.MappingUtils;

@ContextConfiguration(classes = {CartService.class, MongoTemplate.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CartServiceTest {
    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private CartItemRepository cartItemRepository;

    @MockitoBean
    private CartRepository cartRepository;

    @Autowired
    private CartService cartService;

    @MockitoBean
    private MappingUtils mappingUtils;

    @MockitoBean
    private MongoTemplate mongoTemplate;

    @MockitoBean
    private ProductService productService;

    /**
     * Method under test: {@link CartService#postprocessModel(Cart, CartReqDto)}
     */
    @Test
    void testPostprocessModel() {
        // Arrange
        CartReqDto requestDto = new CartReqDto();
        requestDto.setAccountId(UUID.randomUUID().toString());

        Account mockAccount = new Account();
        mockAccount.setId(UUID.fromString(requestDto.getAccountId()));
        when(accountService.find(UUID.fromString(requestDto.getAccountId()), false)).thenReturn(mockAccount);

        Cart model = new Cart();

        // Act
        cartService.postprocessModel(model, requestDto);

        // Assert
        assertSame(mockAccount, model.getAccount());
        verify(accountService).find(UUID.fromString(requestDto.getAccountId()), false);
    }

    /**
     * Method under test:
     * {@link CartService#postprocessUpdateModel(Cart, CartReqDto)}
     */
    @Test
    void testPostprocessUpdateModel() {
        // Arrange
        CartReqDto requestDto = new CartReqDto();
        requestDto.setAccountId(UUID.randomUUID().toString());

        Account mockAccount = new Account();
        mockAccount.setId(UUID.fromString(requestDto.getAccountId()));
        when(accountService.find(UUID.fromString(requestDto.getAccountId()), false)).thenReturn(mockAccount);

        Cart model = new Cart();

        // Act
        cartService.postprocessUpdateModel(model, requestDto);

        // Assert
        assertSame(mockAccount, model.getAccount());
        verify(accountService).find(UUID.fromString(requestDto.getAccountId()), false);
    }

    /**
     * Method under test: {@link CartService#getByToken(UUID)}
     */
    @Test
    void testGetByToken() {
        // Arrange
        when(cartRepository.findAllByAccountId(Mockito.<UUID>any())).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(NotFoundException.class, () -> cartService.getByToken(UUID.randomUUID()));
        verify(cartRepository).findAllByAccountId(isA(UUID.class));
    }

    /**
     * Method under test: {@link CartService#getByToken(UUID)}
     */
    @Test
    void testGetByToken2() {
        // Arrange
        Account account = new Account();
        account.setAddresses(new ArrayList<>());
        account.setBirthYear(2);
        account.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        account.setEmail("jane.doe@example.org");
        account.setGoogleAccount(new GoogleAccount());
        account.setGroup(new AppGroup());
        account.setId(UUID.randomUUID());
        account.setPassword("iloveyou");
        account.setPhoneNumber("6625550144");
        account.setPromotionIds(new HashSet<>());
        account.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account.setUpdatedBy("2020-03-01");
        account.setUsername("janedoe");
        account.setVersion(2);

        GoogleAccount googleAccount = new GoogleAccount();
        googleAccount.setAccount(account);
        googleAccount.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        googleAccount.setEmail("jane.doe@example.org");
        googleAccount.setEmail_verified("jane.doe@example.org");
        googleAccount.setFamily_name("accountId");
        googleAccount.setGiven_name("accountId");
        googleAccount.setId(UUID.randomUUID());
        googleAccount.setName("accountId");
        googleAccount.setPicture("accountId");
        googleAccount.setSub("accountId");
        googleAccount.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount.setUpdatedBy("2020-03-01");
        googleAccount.setVersion(2);

        AppGroup group = new AppGroup();
        group.setAccounts(new ArrayList<>());
        group.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        group.setId(UUID.randomUUID());
        group.setName("accountId");
        group.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group.setUpdatedBy("2020-03-01");
        group.setVersion(2);

        Account account2 = new Account();
        account2.setAddresses(new ArrayList<>());
        account2.setBirthYear(2);
        account2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        account2.setEmail("jane.doe@example.org");
        account2.setGoogleAccount(googleAccount);
        account2.setGroup(group);
        account2.setId(UUID.randomUUID());
        account2.setPassword("iloveyou");
        account2.setPhoneNumber("6625550144");
        account2.setPromotionIds(new HashSet<>());
        account2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account2.setUpdatedBy("2020-03-01");
        account2.setUsername("janedoe");
        account2.setVersion(2);

        Cart cart = new Cart();
        cart.setAccount(account2);
        cart.setCartItems(new ArrayList<>());
        cart.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        cart.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        cart.setId(UUID.randomUUID());
        cart.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        cart.setUpdatedBy("2020-03-01");
        cart.setVersion(2);

        ArrayList<Cart> cartList = new ArrayList<>();
        cartList.add(cart);
        when(cartRepository.findAllByAccountId(Mockito.<UUID>any())).thenReturn(cartList);
        CartResDto cartResDto = new CartResDto();
        when(mappingUtils.mapToDTO(Mockito.<Cart>any(), Mockito.<Class<CartResDto>>any())).thenReturn(cartResDto);

        // Act
        CartResDto actualByToken = cartService.getByToken(UUID.randomUUID());

        // Assert
        verify(cartRepository).findAllByAccountId(isA(UUID.class));
        verify(mappingUtils).mapToDTO(isA(Cart.class), isA(Class.class));
        assertSame(cartResDto, actualByToken);
    }

    /**
     * Method under test:
     * {@link CartService#updateCartItemQuantity(CartQuantityReqDto)}
     */
    @Test
    void testUpdateCartItemQuantity() {
        // Arrange
        CartQuantityReqDto requestDto = new CartQuantityReqDto();
        requestDto.setCartId(UUID.randomUUID());
        requestDto.setProductId(UUID.randomUUID());
        requestDto.setDelta(1);

        Product mockProduct = new Product();
        mockProduct.setId(requestDto.getProductId());
        mockProduct.setQuantity(10);
        mockProduct.setPrice(100.0);
        when(productService.find(requestDto.getProductId(), false)).thenReturn(mockProduct);

        Cart mockCart = new Cart();
        mockCart.setId(requestDto.getCartId());
        when(cartRepository.findById(requestDto.getCartId())).thenReturn(java.util.Optional.of(mockCart));

        CartItem mockCartItem = new CartItem();
        mockCartItem.setProduct(mockProduct);
        mockCartItem.setQuantity(1);
        when(mongoTemplate.findOne(Mockito.any(Query.class), Mockito.eq(CartItem.class))).thenReturn(null);

        CartResDto mockCartResDto = new CartResDto();
        mockCartResDto.setId(mockCart.getId());
        when(mappingUtils.mapToDTO(Mockito.any(Cart.class), Mockito.eq(CartResDto.class))).thenReturn(mockCartResDto);

        // Act
        CartResDto result = cartService.updateCartItemQuantity(requestDto);

        // Assert
        assertSame(mockCart.getId(), result.getId());
        verify(productService).find(requestDto.getProductId(), false);
        verify(cartRepository).save(mockCart);
    }

    /**
     * Method under test: {@link CartService#refreshCart(UUID)}
     */

    @Test
    void testRefreshCart() {
        UUID cartId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setId(cartId);
        cart.setCartItems(new ArrayList<>());
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        CartResDto cartResDto = new CartResDto();
        when(mappingUtils.mapToDTO(any(Cart.class), eq(CartResDto.class))).thenReturn(cartResDto);

        CartResDto result = cartService.refreshCart(cartId);

        verify(cartRepository).findById(eq(cartId));
        verify(cartRepository).save(isA(Cart.class));
        verify(mappingUtils).mapToDTO(isA(Cart.class), eq(CartResDto.class));
        assertSame(cartResDto, result);
    }
}
