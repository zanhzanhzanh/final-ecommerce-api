package org.tdtu.ecommerceapi.service.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tdtu.ecommerceapi.dto.BaseDTO;
import org.tdtu.ecommerceapi.dto.rest.request.PlaceOrderReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.OrderResDto;
import org.tdtu.ecommerceapi.enums.DeliveryStatus;
import org.tdtu.ecommerceapi.enums.PaymentStatus;
import org.tdtu.ecommerceapi.exception.BadRequestException;
import org.tdtu.ecommerceapi.model.Account;
import org.tdtu.ecommerceapi.model.AppGroup;
import org.tdtu.ecommerceapi.model.GoogleAccount;
import org.tdtu.ecommerceapi.model.rest.Address;
import org.tdtu.ecommerceapi.model.rest.Cart;
import org.tdtu.ecommerceapi.model.rest.Order;
import org.tdtu.ecommerceapi.repository.AccountRepository;
import org.tdtu.ecommerceapi.repository.CartItemRepository;
import org.tdtu.ecommerceapi.repository.CartRepository;
import org.tdtu.ecommerceapi.repository.OrderItemRepository;
import org.tdtu.ecommerceapi.repository.OrderRepository;
import org.tdtu.ecommerceapi.utils.MappingUtils;

@ContextConfiguration(classes = {OrderService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class OrderServiceTest {
    @MockitoBean
    private AccountRepository accountRepository;

    @MockitoBean
    private AddressService addressService;

    @MockitoBean
    private CartItemRepository cartItemRepository;

    @MockitoBean
    private CartRepository cartRepository;

    @MockitoBean
    private MappingUtils mappingUtils;

    @MockitoBean
    private OrderItemRepository orderItemRepository;

    @MockitoBean
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @MockitoBean
    private PromotionService promotionService;

    /**
     * Method under test: {@link OrderService#findByAccount(UUID)}
     */
    @Test
    void testFindByAccount() {
        // Arrange
        when(orderRepository.findByAccount(Mockito.<UUID>any())).thenReturn(new ArrayList<>());
        ArrayList<BaseDTO> baseDTOList = new ArrayList<>();
        when(mappingUtils.mapListToDTO(Mockito.<List<Object>>any(), Mockito.<Class<BaseDTO>>any())).thenReturn(baseDTOList);

        // Act
        List<OrderResDto> actualFindByAccountResult = orderService.findByAccount(UUID.randomUUID());

        // Assert
        verify(orderRepository).findByAccount(isA(UUID.class));
        verify(mappingUtils).mapListToDTO(isA(List.class), isA(Class.class));
        assertTrue(actualFindByAccountResult.isEmpty());
        assertSame(baseDTOList, actualFindByAccountResult);
    }

    /**
     * Method under test: {@link OrderService#findByAccount(UUID)}
     */
    @Test
    void testFindByAccount2() {
        // Arrange
        when(orderRepository.findByAccount(Mockito.<UUID>any())).thenReturn(new ArrayList<>());
        when(mappingUtils.mapListToDTO(Mockito.<List<Object>>any(), Mockito.<Class<BaseDTO>>any()))
                .thenThrow(new BadRequestException("An error occurred"));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> orderService.findByAccount(UUID.randomUUID()));
        verify(orderRepository).findByAccount(isA(UUID.class));
        verify(mappingUtils).mapListToDTO(isA(List.class), isA(Class.class));
    }

    /**
     * Method under test: {@link OrderService#getAvailablePromotions(UUID, List)}
     */
    @Test
    void testGetAvailablePromotions() {
        // Arrange
        GoogleAccount googleAccount = new GoogleAccount();
        googleAccount.setAccount(new Account());
        googleAccount.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        googleAccount.setEmail("jane.doe@example.org");
        googleAccount.setEmail_verified("jane.doe@example.org");
        googleAccount.setFamily_name("Family name");
        googleAccount.setGiven_name("Given name");
        googleAccount.setId(UUID.randomUUID());
        googleAccount.setName("Name");
        googleAccount.setPicture("Picture");
        googleAccount.setSub("Sub");
        googleAccount.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount.setUpdatedBy("2020-03-01");
        googleAccount.setVersion(1);

        AppGroup group = new AppGroup();
        group.setAccounts(new ArrayList<>());
        group.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        group.setId(UUID.randomUUID());
        group.setName("Name");
        group.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group.setUpdatedBy("2020-03-01");
        group.setVersion(1);

        Account account = new Account();
        account.setAddresses(new ArrayList<>());
        account.setBirthYear(1);
        account.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        account.setEmail("jane.doe@example.org");
        account.setGoogleAccount(googleAccount);
        account.setGroup(group);
        account.setId(UUID.randomUUID());
        account.setPassword("iloveyou");
        account.setPhoneNumber("6625550144");
        account.setPromotionIds(new HashSet<>());
        account.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account.setUpdatedBy("2020-03-01");
        account.setUsername("janedoe");
        account.setVersion(1);

        GoogleAccount googleAccount2 = new GoogleAccount();
        googleAccount2.setAccount(account);
        googleAccount2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        googleAccount2.setEmail("jane.doe@example.org");
        googleAccount2.setEmail_verified("jane.doe@example.org");
        googleAccount2.setFamily_name("Family name");
        googleAccount2.setGiven_name("Given name");
        googleAccount2.setId(UUID.randomUUID());
        googleAccount2.setName("Name");
        googleAccount2.setPicture("Picture");
        googleAccount2.setSub("Sub");
        googleAccount2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount2.setUpdatedBy("2020-03-01");
        googleAccount2.setVersion(1);

        AppGroup group2 = new AppGroup();
        group2.setAccounts(new ArrayList<>());
        group2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        group2.setId(UUID.randomUUID());
        group2.setName("Name");
        group2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group2.setUpdatedBy("2020-03-01");
        group2.setVersion(1);

        Account account2 = new Account();
        account2.setAddresses(new ArrayList<>());
        account2.setBirthYear(1);
        account2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        account2.setEmail("jane.doe@example.org");
        account2.setGoogleAccount(googleAccount2);
        account2.setGroup(group2);
        account2.setId(UUID.randomUUID());
        account2.setPassword("iloveyou");
        account2.setPhoneNumber("6625550144");
        account2.setPromotionIds(new HashSet<>());
        account2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account2.setUpdatedBy("2020-03-01");
        account2.setUsername("janedoe");
        account2.setVersion(1);
        Optional<Account> ofResult = Optional.of(account2);
        when(accountRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        UUID accountId = UUID.randomUUID();

        // Act
        List<UUID> actualAvailablePromotions = orderService.getAvailablePromotions(accountId, new ArrayList<>());

        // Assert
        verify(accountRepository).findById(isA(UUID.class));
        assertTrue(actualAvailablePromotions.isEmpty());
    }

    /**
     * Method under test: {@link OrderService#calTempTotalPrice(PlaceOrderReqDto)}
     */
    @Test
    void testCalTempTotalPrice() {
        // Arrange
        Account account = new Account();
        account.setAddresses(new ArrayList<>());
        account.setBirthYear(1);
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
        account.setVersion(1);

        GoogleAccount googleAccount = new GoogleAccount();
        googleAccount.setAccount(account);
        googleAccount.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        googleAccount.setEmail("jane.doe@example.org");
        googleAccount.setEmail_verified("jane.doe@example.org");
        googleAccount.setFamily_name("Family name");
        googleAccount.setGiven_name("Given name");
        googleAccount.setId(UUID.randomUUID());
        googleAccount.setName("Name");
        googleAccount.setPicture("Picture");
        googleAccount.setSub("Sub");
        googleAccount.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount.setUpdatedBy("2020-03-01");
        googleAccount.setVersion(1);

        AppGroup group = new AppGroup();
        group.setAccounts(new ArrayList<>());
        group.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        group.setId(UUID.randomUUID());
        group.setName("Name");
        group.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group.setUpdatedBy("2020-03-01");
        group.setVersion(1);

        Account account2 = new Account();
        account2.setAddresses(new ArrayList<>());
        account2.setBirthYear(1);
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
        account2.setVersion(1);

        Cart cart = new Cart();
        cart.setAccount(account2);
        cart.setCartItems(new ArrayList<>());
        cart.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        cart.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        cart.setId(UUID.randomUUID());
        cart.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        cart.setUpdatedBy("2020-03-01");
        cart.setVersion(1);
        Optional<Cart> ofResult = Optional.of(cart);
        when(cartRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);

        GoogleAccount googleAccount2 = new GoogleAccount();
        googleAccount2.setAccount(new Account());
        googleAccount2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        googleAccount2.setEmail("jane.doe@example.org");
        googleAccount2.setEmail_verified("jane.doe@example.org");
        googleAccount2.setFamily_name("Family name");
        googleAccount2.setGiven_name("Given name");
        googleAccount2.setId(UUID.randomUUID());
        googleAccount2.setName("Name");
        googleAccount2.setPicture("Picture");
        googleAccount2.setSub("Sub");
        googleAccount2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount2.setUpdatedBy("2020-03-01");
        googleAccount2.setVersion(1);

        AppGroup group2 = new AppGroup();
        group2.setAccounts(new ArrayList<>());
        group2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        group2.setId(UUID.randomUUID());
        group2.setName("Name");
        group2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group2.setUpdatedBy("2020-03-01");
        group2.setVersion(1);

        Account account3 = new Account();
        account3.setAddresses(new ArrayList<>());
        account3.setBirthYear(1);
        account3.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account3.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        account3.setEmail("jane.doe@example.org");
        account3.setGoogleAccount(googleAccount2);
        account3.setGroup(group2);
        account3.setId(UUID.randomUUID());
        account3.setPassword("iloveyou");
        account3.setPhoneNumber("6625550144");
        account3.setPromotionIds(new HashSet<>());
        account3.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account3.setUpdatedBy("2020-03-01");
        account3.setUsername("janedoe");
        account3.setVersion(1);

        GoogleAccount googleAccount3 = new GoogleAccount();
        googleAccount3.setAccount(account3);
        googleAccount3.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount3.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        googleAccount3.setEmail("jane.doe@example.org");
        googleAccount3.setEmail_verified("jane.doe@example.org");
        googleAccount3.setFamily_name("Family name");
        googleAccount3.setGiven_name("Given name");
        googleAccount3.setId(UUID.randomUUID());
        googleAccount3.setName("Name");
        googleAccount3.setPicture("Picture");
        googleAccount3.setSub("Sub");
        googleAccount3.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount3.setUpdatedBy("2020-03-01");
        googleAccount3.setVersion(1);

        AppGroup group3 = new AppGroup();
        group3.setAccounts(new ArrayList<>());
        group3.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group3.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        group3.setId(UUID.randomUUID());
        group3.setName("Name");
        group3.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group3.setUpdatedBy("2020-03-01");
        group3.setVersion(1);

        Account account4 = new Account();
        account4.setAddresses(new ArrayList<>());
        account4.setBirthYear(1);
        account4.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account4.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        account4.setEmail("jane.doe@example.org");
        account4.setGoogleAccount(googleAccount3);
        account4.setGroup(group3);
        account4.setId(UUID.randomUUID());
        account4.setPassword("iloveyou");
        account4.setPhoneNumber("6625550144");
        account4.setPromotionIds(new HashSet<>());
        account4.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account4.setUpdatedBy("2020-03-01");
        account4.setUsername("janedoe");
        account4.setVersion(1);
        Optional<Account> ofResult2 = Optional.of(account4);
        when(accountRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult2);

        // Act
        double actualCalTempTotalPriceResult = orderService.calTempTotalPrice(new PlaceOrderReqDto());

        // Assert
        verify(accountRepository).findById(isA(UUID.class));
        verify(cartRepository).findById(isNull());
        assertEquals(0.0d, actualCalTempTotalPriceResult);
    }

    /**
     * Method under test: {@link OrderService#calTempTotalPrice(PlaceOrderReqDto)}
     */
    @Test
    void testCalTempTotalPrice2() {
        // Arrange
        Account account = new Account();
        account.setAddresses(new ArrayList<>());
        account.setBirthYear(1);
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
        account.setVersion(1);

        GoogleAccount googleAccount = new GoogleAccount();
        googleAccount.setAccount(account);
        googleAccount.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        googleAccount.setEmail("jane.doe@example.org");
        googleAccount.setEmail_verified("jane.doe@example.org");
        googleAccount.setFamily_name("Family name");
        googleAccount.setGiven_name("Given name");
        googleAccount.setId(UUID.randomUUID());
        googleAccount.setName("Name");
        googleAccount.setPicture("Picture");
        googleAccount.setSub("Sub");
        googleAccount.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount.setUpdatedBy("2020-03-01");
        googleAccount.setVersion(1);

        AppGroup group = new AppGroup();
        group.setAccounts(new ArrayList<>());
        group.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        group.setId(UUID.randomUUID());
        group.setName("Name");
        group.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group.setUpdatedBy("2020-03-01");
        group.setVersion(1);

        Account account2 = new Account();
        account2.setAddresses(new ArrayList<>());
        account2.setBirthYear(1);
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
        account2.setVersion(1);

        Cart cart = new Cart();
        cart.setAccount(account2);
        cart.setCartItems(new ArrayList<>());
        cart.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        cart.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        cart.setId(UUID.randomUUID());
        cart.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        cart.setUpdatedBy("2020-03-01");
        cart.setVersion(1);
        Optional<Cart> ofResult = Optional.of(cart);
        when(cartRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        when(accountRepository.findById(Mockito.<UUID>any())).thenThrow(new BadRequestException("An error occurred"));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> orderService.calTempTotalPrice(new PlaceOrderReqDto()));
        verify(accountRepository).findById(isA(UUID.class));
        verify(cartRepository).findById(isNull());
    }

    /**
     * Method under test: {@link OrderService#placeOrder(PlaceOrderReqDto)}
     */
    @Test
    void testPlaceOrder() {
        // Arrange
        Account account = new Account();
        account.setAddresses(new ArrayList<>());
        account.setBirthYear(1);
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
        account.setVersion(1);

        GoogleAccount googleAccount = new GoogleAccount();
        googleAccount.setAccount(account);
        googleAccount.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        googleAccount.setEmail("jane.doe@example.org");
        googleAccount.setEmail_verified("jane.doe@example.org");
        googleAccount.setFamily_name("Family name");
        googleAccount.setGiven_name("Given name");
        googleAccount.setId(UUID.randomUUID());
        googleAccount.setName("Name");
        googleAccount.setPicture("Picture");
        googleAccount.setSub("Sub");
        googleAccount.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount.setUpdatedBy("2020-03-01");
        googleAccount.setVersion(1);

        AppGroup group = new AppGroup();
        group.setAccounts(new ArrayList<>());
        group.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        group.setId(UUID.randomUUID());
        group.setName("Name");
        group.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group.setUpdatedBy("2020-03-01");
        group.setVersion(1);

        Account account2 = new Account();
        account2.setAddresses(new ArrayList<>());
        account2.setBirthYear(1);
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
        account2.setVersion(1);

        Cart cart = new Cart();
        cart.setAccount(account2);
        cart.setCartItems(new ArrayList<>());
        cart.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        cart.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        cart.setId(UUID.randomUUID());
        cart.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        cart.setUpdatedBy("2020-03-01");
        cart.setVersion(1);
        Optional<Cart> ofResult = Optional.of(cart);
        when(cartRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);

        GoogleAccount googleAccount2 = new GoogleAccount();
        googleAccount2.setAccount(new Account());
        googleAccount2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        googleAccount2.setEmail("jane.doe@example.org");
        googleAccount2.setEmail_verified("jane.doe@example.org");
        googleAccount2.setFamily_name("Family name");
        googleAccount2.setGiven_name("Given name");
        googleAccount2.setId(UUID.randomUUID());
        googleAccount2.setName("Name");
        googleAccount2.setPicture("Picture");
        googleAccount2.setSub("Sub");
        googleAccount2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount2.setUpdatedBy("2020-03-01");
        googleAccount2.setVersion(1);

        AppGroup group2 = new AppGroup();
        group2.setAccounts(new ArrayList<>());
        group2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        group2.setId(UUID.randomUUID());
        group2.setName("Name");
        group2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group2.setUpdatedBy("2020-03-01");
        group2.setVersion(1);

        Account account3 = new Account();
        account3.setAddresses(new ArrayList<>());
        account3.setBirthYear(1);
        account3.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account3.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        account3.setEmail("jane.doe@example.org");
        account3.setGoogleAccount(googleAccount2);
        account3.setGroup(group2);
        account3.setId(UUID.randomUUID());
        account3.setPassword("iloveyou");
        account3.setPhoneNumber("6625550144");
        account3.setPromotionIds(new HashSet<>());
        account3.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account3.setUpdatedBy("2020-03-01");
        account3.setUsername("janedoe");
        account3.setVersion(1);

        GoogleAccount googleAccount3 = new GoogleAccount();
        googleAccount3.setAccount(account3);
        googleAccount3.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount3.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        googleAccount3.setEmail("jane.doe@example.org");
        googleAccount3.setEmail_verified("jane.doe@example.org");
        googleAccount3.setFamily_name("Family name");
        googleAccount3.setGiven_name("Given name");
        googleAccount3.setId(UUID.randomUUID());
        googleAccount3.setName("Name");
        googleAccount3.setPicture("Picture");
        googleAccount3.setSub("Sub");
        googleAccount3.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount3.setUpdatedBy("2020-03-01");
        googleAccount3.setVersion(1);

        AppGroup group3 = new AppGroup();
        group3.setAccounts(new ArrayList<>());
        group3.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group3.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        group3.setId(UUID.randomUUID());
        group3.setName("Name");
        group3.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group3.setUpdatedBy("2020-03-01");
        group3.setVersion(1);

        Account account4 = new Account();
        account4.setAddresses(new ArrayList<>());
        account4.setBirthYear(1);
        account4.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account4.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        account4.setEmail("jane.doe@example.org");
        account4.setGoogleAccount(googleAccount3);
        account4.setGroup(group3);
        account4.setId(UUID.randomUUID());
        account4.setPassword("iloveyou");
        account4.setPhoneNumber("6625550144");
        account4.setPromotionIds(new HashSet<>());
        account4.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account4.setUpdatedBy("2020-03-01");
        account4.setUsername("janedoe");
        account4.setVersion(1);
        Optional<Account> ofResult2 = Optional.of(account4);
        when(accountRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(BadRequestException.class, () -> orderService.placeOrder(new PlaceOrderReqDto()));
        verify(accountRepository).findById(isA(UUID.class));
        verify(cartRepository).findById(isNull());
    }

    /**
     * Method under test: {@link OrderService#placeOrder(PlaceOrderReqDto)}
     */
    @Test
    void testPlaceOrder2() {
        // Arrange
        Account account = new Account();
        account.setAddresses(new ArrayList<>());
        account.setBirthYear(1);
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
        account.setVersion(1);

        GoogleAccount googleAccount = new GoogleAccount();
        googleAccount.setAccount(account);
        googleAccount.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        googleAccount.setEmail("jane.doe@example.org");
        googleAccount.setEmail_verified("jane.doe@example.org");
        googleAccount.setFamily_name("Family name");
        googleAccount.setGiven_name("Given name");
        googleAccount.setId(UUID.randomUUID());
        googleAccount.setName("Name");
        googleAccount.setPicture("Picture");
        googleAccount.setSub("Sub");
        googleAccount.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount.setUpdatedBy("2020-03-01");
        googleAccount.setVersion(1);

        AppGroup group = new AppGroup();
        group.setAccounts(new ArrayList<>());
        group.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        group.setId(UUID.randomUUID());
        group.setName("Name");
        group.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group.setUpdatedBy("2020-03-01");
        group.setVersion(1);

        Account account2 = new Account();
        account2.setAddresses(new ArrayList<>());
        account2.setBirthYear(1);
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
        account2.setVersion(1);

        Cart cart = new Cart();
        cart.setAccount(account2);
        cart.setCartItems(new ArrayList<>());
        cart.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        cart.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        cart.setId(UUID.randomUUID());
        cart.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        cart.setUpdatedBy("2020-03-01");
        cart.setVersion(1);
        Optional<Cart> ofResult = Optional.of(cart);
        when(cartRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        when(accountRepository.findById(Mockito.<UUID>any())).thenThrow(new BadRequestException("An error occurred"));

        // Act and Assert
        assertThrows(BadRequestException.class, () -> orderService.placeOrder(new PlaceOrderReqDto()));
        verify(accountRepository).findById(isA(UUID.class));
        verify(cartRepository).findById(isNull());
    }

    /**
     * Method under test:
     * {@link OrderService#updateOrderStatus(UUID, PaymentStatus, DeliveryStatus)}
     */
    @Test
    void testUpdateOrderStatus() {
        // Arrange
        Account account = new Account();
        account.setAddresses(new ArrayList<>());
        account.setBirthYear(1);
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
        account.setVersion(1);

        GoogleAccount googleAccount = new GoogleAccount();
        googleAccount.setAccount(account);
        googleAccount.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        googleAccount.setEmail("jane.doe@example.org");
        googleAccount.setEmail_verified("jane.doe@example.org");
        googleAccount.setFamily_name("Family name");
        googleAccount.setGiven_name("Given name");
        googleAccount.setId(UUID.randomUUID());
        googleAccount.setName("Name");
        googleAccount.setPicture("Picture");
        googleAccount.setSub("Sub");
        googleAccount.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount.setUpdatedBy("2020-03-01");
        googleAccount.setVersion(1);

        AppGroup group = new AppGroup();
        group.setAccounts(new ArrayList<>());
        group.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        group.setId(UUID.randomUUID());
        group.setName("Name");
        group.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group.setUpdatedBy("2020-03-01");
        group.setVersion(1);

        Account account2 = new Account();
        account2.setAddresses(new ArrayList<>());
        account2.setBirthYear(1);
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
        account2.setVersion(1);

        GoogleAccount googleAccount2 = new GoogleAccount();
        googleAccount2.setAccount(new Account());
        googleAccount2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        googleAccount2.setEmail("jane.doe@example.org");
        googleAccount2.setEmail_verified("jane.doe@example.org");
        googleAccount2.setFamily_name("Family name");
        googleAccount2.setGiven_name("Given name");
        googleAccount2.setId(UUID.randomUUID());
        googleAccount2.setName("Name");
        googleAccount2.setPicture("Picture");
        googleAccount2.setSub("Sub");
        googleAccount2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        googleAccount2.setUpdatedBy("2020-03-01");
        googleAccount2.setVersion(1);

        AppGroup group2 = new AppGroup();
        group2.setAccounts(new ArrayList<>());
        group2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        group2.setId(UUID.randomUUID());
        group2.setName("Name");
        group2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        group2.setUpdatedBy("2020-03-01");
        group2.setVersion(1);

        Account account3 = new Account();
        account3.setAddresses(new ArrayList<>());
        account3.setBirthYear(1);
        account3.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account3.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        account3.setEmail("jane.doe@example.org");
        account3.setGoogleAccount(googleAccount2);
        account3.setGroup(group2);
        account3.setId(UUID.randomUUID());
        account3.setPassword("iloveyou");
        account3.setPhoneNumber("6625550144");
        account3.setPromotionIds(new HashSet<>());
        account3.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account3.setUpdatedBy("2020-03-01");
        account3.setUsername("janedoe");
        account3.setVersion(1);

        Address address = new Address();
        address.setAccount(account3);
        address.setBuildingName("Building Name");
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        address.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        address.setId(UUID.randomUUID());
        address.setPincode("Pincode");
        address.setState("MD");
        address.setStreet("Street");
        address.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        address.setUpdatedBy("2020-03-01");
        address.setVersion(1);

        Order order = new Order();
        order.setAccount(account2);
        order.setAddress(address);
        order.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        order.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        order.setDeliveryStatus(DeliveryStatus.PENDING);
        order.setId(UUID.randomUUID());
        order.setOrderDate(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        order.setOrderItems(new ArrayList<>());
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setShipCOD(true);
        order.setTotalPrice(10.0d);
        order.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        order.setUpdatedBy("2020-03-01");
        order.setUsedPromotions(new HashSet<>());
        order.setVersion(1);
        Optional<Order> ofResult = Optional.of(order);
        when(orderRepository.save(Mockito.<Order>any())).thenThrow(new BadRequestException("An error occurred"));
        when(orderRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(BadRequestException.class,
                () -> orderService.updateOrderStatus(UUID.randomUUID(), PaymentStatus.PENDING, DeliveryStatus.PENDING));
        verify(orderRepository).findById(isA(UUID.class));
        verify(orderRepository).save(isA(Order.class));
    }
}
