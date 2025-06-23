package org.tdtu.ecommerceapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tdtu.ecommerceapi.dto.BaseDTO;
import org.tdtu.ecommerceapi.dto.admin.request.GroupRequestDTO;
import org.tdtu.ecommerceapi.dto.admin.response.GroupResponseDTO;
import org.tdtu.ecommerceapi.dto.api.ApiPageableResponse;
import org.tdtu.ecommerceapi.dto.rest.request.CartItemReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.CartItemResDto;
import org.tdtu.ecommerceapi.exception.NotFoundException;
import org.tdtu.ecommerceapi.model.Account;
import org.tdtu.ecommerceapi.model.AppGroup;
import org.tdtu.ecommerceapi.model.BaseModel;
import org.tdtu.ecommerceapi.model.GoogleAccount;
import org.tdtu.ecommerceapi.model.rest.Cart;
import org.tdtu.ecommerceapi.model.rest.CartItem;
import org.tdtu.ecommerceapi.model.rest.Category;
import org.tdtu.ecommerceapi.model.rest.Product;
import org.tdtu.ecommerceapi.repository.CartItemRepository;
import org.tdtu.ecommerceapi.repository.GroupRepository;
import org.tdtu.ecommerceapi.service.rest.CartItemService;
import org.tdtu.ecommerceapi.utils.MappingUtils;

@ContextConfiguration(classes = {GroupService.class, CartItemService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class BaseServiceTest {
    @MockitoBean
    private CartItemRepository cartItemRepository;

    @Autowired
    private BaseService<AppGroup, GroupRequestDTO, GroupResponseDTO, GroupRepository> baseService;

    @Autowired
    private BaseService<CartItem, CartItemReqDto, CartItemResDto, CartItemRepository> cartItemService;

    @MockitoBean
    private GroupRepository groupRepository;

    @MockitoBean
    private MappingUtils mappingUtils;

    /**
     * Method under test: {@link BaseService#create(BaseDTO)}
     */
    @Test
    void testCreate() {
        // Arrange
        AppGroup appGroup = new AppGroup();
        appGroup.setAccounts(new ArrayList<>());
        appGroup.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup.setId(UUID.randomUUID());
        appGroup.setName("Name");
        appGroup.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setUpdatedBy("2020-03-01");
        appGroup.setVersion(1);
        when(groupRepository.save(Mockito.<AppGroup>any())).thenReturn(appGroup);

        AppGroup appGroup2 = new AppGroup();
        appGroup2.setAccounts(new ArrayList<>());
        appGroup2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup2.setId(UUID.randomUUID());
        appGroup2.setName("Name");
        appGroup2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup2.setUpdatedBy("2020-03-01");
        appGroup2.setVersion(1);
        when(mappingUtils.mapFromDTO(Mockito.<GroupRequestDTO>any(), Mockito.<Class<AppGroup>>any())).thenReturn(appGroup2);
        GroupResponseDTO groupResponseDTO = new GroupResponseDTO();
        when(mappingUtils.mapToDTO(Mockito.<AppGroup>any(), Mockito.<Class<GroupResponseDTO>>any()))
                .thenReturn(groupResponseDTO);

        // Act
        GroupResponseDTO actualCreateResult = baseService.create(new GroupRequestDTO());

        // Assert
        verify(groupRepository).save(isA(AppGroup.class));
        verify(mappingUtils).mapFromDTO(isA(GroupRequestDTO.class), isA(Class.class));
        verify(mappingUtils).mapToDTO(isA(AppGroup.class), isA(Class.class));
        assertSame(groupResponseDTO, actualCreateResult);
    }

    /**
     * Method under test: {@link BaseService#create(BaseModel)}
     */
    @Test
    void testCreate2() {
        // Arrange
        AppGroup appGroup = new AppGroup();
        appGroup.setAccounts(new ArrayList<>());
        appGroup.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup.setId(UUID.randomUUID());
        appGroup.setName("Name");
        appGroup.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setUpdatedBy("2020-03-01");
        appGroup.setVersion(1);
        when(groupRepository.save(Mockito.<AppGroup>any())).thenReturn(appGroup);
        GroupResponseDTO groupResponseDTO = new GroupResponseDTO();
        when(mappingUtils.mapToDTO(Mockito.<AppGroup>any(), Mockito.<Class<GroupResponseDTO>>any()))
                .thenReturn(groupResponseDTO);

        AppGroup appGroup2 = new AppGroup();
        appGroup2.setAccounts(new ArrayList<>());
        appGroup2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup2.setId(UUID.randomUUID());
        appGroup2.setName("Name");
        appGroup2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup2.setUpdatedBy("2020-03-01");
        appGroup2.setVersion(1);

        // Act
        GroupResponseDTO actualCreateResult = baseService.create(appGroup2);

        // Assert
        verify(groupRepository).save(isA(AppGroup.class));
        verify(mappingUtils).mapToDTO(isA(AppGroup.class), isA(Class.class));
        assertSame(groupResponseDTO, actualCreateResult);
    }

    /**
     * Method under test: {@link BaseService#create(BaseModel)}
     */
    @Test
    void testCreate3() {
        // Arrange
        AppGroup appGroup = new AppGroup();
        appGroup.setAccounts(new ArrayList<>());
        appGroup.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup.setId(UUID.randomUUID());
        appGroup.setName("Name");
        appGroup.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setUpdatedBy("2020-03-01");
        appGroup.setVersion(1);
        when(groupRepository.save(Mockito.<AppGroup>any())).thenReturn(appGroup);
        Class<Object> clazz = Object.class;
        when(mappingUtils.mapToDTO(Mockito.<AppGroup>any(), Mockito.<Class<GroupResponseDTO>>any()))
                .thenThrow(new NotFoundException(clazz));

        AppGroup appGroup2 = new AppGroup();
        appGroup2.setAccounts(new ArrayList<>());
        appGroup2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup2.setId(UUID.randomUUID());
        appGroup2.setName("Name");
        appGroup2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup2.setUpdatedBy("2020-03-01");
        appGroup2.setVersion(1);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> baseService.create(appGroup2));
        verify(groupRepository).save(isA(AppGroup.class));
        verify(mappingUtils).mapToDTO(isA(AppGroup.class), isA(Class.class));
    }

    /**
     * Method under test: {@link BaseService#getById(UUID, boolean)}
     */
    @Test
    void testGetById() {
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

        Cart cart = new Cart();
        cart.setAccount(account);
        cart.setCartItems(new ArrayList<>());
        cart.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        cart.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        cart.setId(UUID.randomUUID());
        cart.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        cart.setUpdatedBy("2020-03-01");
        cart.setVersion(1);

        Category category = new Category();
        category.setCategoryName("Category Name");
        category.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        category.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        category.setId(UUID.randomUUID());
        category.setProducts(new ArrayList<>());
        category.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        category.setUpdatedBy("2020-03-01");
        category.setVersion(1);

        Product product = new Product();
        product.setCategory(category);
        product.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        product.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        product.setDescription("The characteristics of someone or something");
        product.setId(UUID.randomUUID());
        product.setImage("Image");
        product.setPrice(10.0d);
        product.setProductName("Product Name");
        product.setQuantity(1);
        product.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        product.setUpdatedBy("2020-03-01");
        product.setVersion(1);

        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        cartItem.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        cartItem.setId(UUID.randomUUID());
        cartItem.setProduct(product);
        cartItem.setProductPrice(10.0d);
        cartItem.setQuantity(1);
        cartItem.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        cartItem.setUpdatedBy("2020-03-01");
        cartItem.setVersion(1);
        Optional<CartItem> ofResult = Optional.of(cartItem);
        when(cartItemRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);
        CartItemResDto cartItemResDto = new CartItemResDto();
        when(mappingUtils.mapToDTO(Mockito.<CartItem>any(), Mockito.<Class<CartItemResDto>>any()))
                .thenReturn(cartItemResDto);

        // Act
        CartItemResDto actualById = cartItemService.getById(UUID.randomUUID(), true);

        // Assert
        verify(cartItemRepository).findById(isA(UUID.class));
        verify(mappingUtils).mapToDTO(isA(CartItem.class), isA(Class.class));
        assertSame(cartItemResDto, actualById);
    }

    /**
     * Method under test: {@link BaseService#updateById(UUID, BaseDTO)}
     */
    @Test
    void testUpdateById() {
        // Arrange
        AppGroup appGroup = new AppGroup();
        appGroup.setAccounts(new ArrayList<>());
        appGroup.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup.setId(UUID.randomUUID());
        appGroup.setName("Name");
        appGroup.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setUpdatedBy("2020-03-01");
        appGroup.setVersion(1);
        Optional<AppGroup> ofResult = Optional.of(appGroup);

        AppGroup appGroup2 = new AppGroup();
        appGroup2.setAccounts(new ArrayList<>());
        appGroup2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup2.setId(UUID.randomUUID());
        appGroup2.setName("Name");
        appGroup2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup2.setUpdatedBy("2020-03-01");
        appGroup2.setVersion(1);
        when(groupRepository.save(Mockito.<AppGroup>any())).thenReturn(appGroup2);
        when(groupRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);

        AppGroup appGroup3 = new AppGroup();
        appGroup3.setAccounts(new ArrayList<>());
        appGroup3.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup3.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup3.setId(UUID.randomUUID());
        appGroup3.setName("Name");
        appGroup3.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup3.setUpdatedBy("2020-03-01");
        appGroup3.setVersion(1);
        GroupResponseDTO groupResponseDTO = new GroupResponseDTO();
        when(mappingUtils.mapToDTO(Mockito.<AppGroup>any(), Mockito.<Class<GroupResponseDTO>>any()))
                .thenReturn(groupResponseDTO);
        when(mappingUtils.mapFromDTO(Mockito.<GroupRequestDTO>any(), Mockito.<Class<AppGroup>>any())).thenReturn(appGroup3);
        when(mappingUtils.getSimpleMapper()).thenReturn(new ModelMapper());
        UUID id = UUID.randomUUID();

        // Act
        GroupResponseDTO actualUpdateByIdResult = baseService.updateById(id, new GroupRequestDTO());

        // Assert
        verify(groupRepository).findById(isA(UUID.class));
        verify(groupRepository).save(isA(AppGroup.class));
        verify(mappingUtils).getSimpleMapper();
        verify(mappingUtils).mapFromDTO(isA(GroupRequestDTO.class), isA(Class.class));
        verify(mappingUtils).mapToDTO(isA(AppGroup.class), isA(Class.class));
        assertSame(groupResponseDTO, actualUpdateByIdResult);
    }

    /**
     * Method under test: {@link BaseService#updateById(UUID, BaseDTO)}
     */
    @Test
    void testUpdateById2() {
        // Arrange
        AppGroup appGroup = new AppGroup();
        appGroup.setAccounts(new ArrayList<>());
        appGroup.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup.setId(UUID.randomUUID());
        appGroup.setName("Name");
        appGroup.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setUpdatedBy("2020-03-01");
        appGroup.setVersion(1);

        AppGroup appGroup2 = new AppGroup();
        appGroup2.setAccounts(new ArrayList<>());
        appGroup2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup2.setId(UUID.randomUUID());
        appGroup2.setName("Name");
        appGroup2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup2.setUpdatedBy("2020-03-01");
        appGroup2.setVersion(1);
        AppGroup appGroup3 = mock(AppGroup.class);
        when(appGroup3.getVersion()).thenReturn(1);
        when(appGroup3.getName()).thenReturn("Name");
        when(appGroup3.getCreatedBy()).thenReturn("Jan 1, 2020 8:00am GMT+0100");
        when(appGroup3.getUpdatedBy()).thenReturn("2020-03-01");
        when(appGroup3.getCreatedAt()).thenReturn(null);
        when(appGroup3.getUpdatedAt())
                .thenReturn(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        when(appGroup3.getAccounts()).thenReturn(new ArrayList<>());
        when(appGroup3.getId()).thenReturn(UUID.randomUUID());
        when(appGroup3.setAccounts(Mockito.<List<Account>>any())).thenReturn(appGroup);
        when(appGroup3.setName(Mockito.<String>any())).thenReturn(appGroup2);
        doNothing().when(appGroup3).setCreatedAt(Mockito.<OffsetDateTime>any());
        doNothing().when(appGroup3).setCreatedBy(Mockito.<String>any());
        doNothing().when(appGroup3).setId(Mockito.<UUID>any());
        doNothing().when(appGroup3).setUpdatedAt(Mockito.<OffsetDateTime>any());
        doNothing().when(appGroup3).setUpdatedBy(Mockito.<String>any());
        doNothing().when(appGroup3).setVersion(Mockito.<Integer>any());
        appGroup3.setAccounts(new ArrayList<>());
        appGroup3.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup3.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup3.setId(UUID.randomUUID());
        appGroup3.setName("Name");
        appGroup3.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup3.setUpdatedBy("2020-03-01");
        appGroup3.setVersion(1);
        Optional<AppGroup> ofResult = Optional.of(appGroup3);

        AppGroup appGroup4 = new AppGroup();
        appGroup4.setAccounts(new ArrayList<>());
        appGroup4.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup4.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup4.setId(UUID.randomUUID());
        appGroup4.setName("Name");
        appGroup4.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup4.setUpdatedBy("2020-03-01");
        appGroup4.setVersion(1);
        when(groupRepository.save(Mockito.<AppGroup>any())).thenReturn(appGroup4);
        when(groupRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);

        AppGroup appGroup5 = new AppGroup();
        appGroup5.setAccounts(new ArrayList<>());
        appGroup5.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup5.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup5.setId(UUID.randomUUID());
        appGroup5.setName("Name");
        appGroup5.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup5.setUpdatedBy("2020-03-01");
        appGroup5.setVersion(1);
        GroupResponseDTO groupResponseDTO = new GroupResponseDTO();
        when(mappingUtils.mapToDTO(Mockito.<AppGroup>any(), Mockito.<Class<GroupResponseDTO>>any()))
                .thenReturn(groupResponseDTO);
        when(mappingUtils.mapFromDTO(Mockito.<GroupRequestDTO>any(), Mockito.<Class<AppGroup>>any())).thenReturn(appGroup5);
        when(mappingUtils.getSimpleMapper()).thenReturn(new ModelMapper());
        UUID id = UUID.randomUUID();

        // Act
        GroupResponseDTO actualUpdateByIdResult = baseService.updateById(id, new GroupRequestDTO());

        // Assert
        verify(groupRepository).findById(isA(UUID.class));
        verify(groupRepository).save(isA(AppGroup.class));
        verify(appGroup3).getAccounts();
        verify(appGroup3).getName();
        verify(appGroup3, atLeast(1)).setAccounts(isA(List.class));
        verify(appGroup3, atLeast(1)).setName(eq("Name"));
        verify(appGroup3).getCreatedAt();
        verify(appGroup3).getCreatedBy();
        verify(appGroup3).getId();
        verify(appGroup3).getUpdatedAt();
        verify(appGroup3).getUpdatedBy();
        verify(appGroup3).getVersion();
        verify(appGroup3, atLeast(1)).setCreatedAt(isA(OffsetDateTime.class));
        verify(appGroup3, atLeast(1)).setCreatedBy(eq("Jan 1, 2020 8:00am GMT+0100"));
        verify(appGroup3, atLeast(1)).setId(Mockito.<UUID>any());
        verify(appGroup3, atLeast(1)).setUpdatedAt(isA(OffsetDateTime.class));
        verify(appGroup3, atLeast(1)).setUpdatedBy(eq("2020-03-01"));
        verify(appGroup3, atLeast(1)).setVersion(eq(1));
        verify(mappingUtils).getSimpleMapper();
        verify(mappingUtils).mapFromDTO(isA(GroupRequestDTO.class), isA(Class.class));
        verify(mappingUtils).mapToDTO(isA(AppGroup.class), isA(Class.class));
        assertSame(groupResponseDTO, actualUpdateByIdResult);
    }

    /**
     * Method under test:
     * {@link BaseService#preprocessUpdateModel(BaseModel, BaseDTO)}
     */
    @Test
    void testPreprocessUpdateModel() {
        // Arrange
        AppGroup appGroup = new AppGroup();
        appGroup.setAccounts(new ArrayList<>());
        appGroup.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup.setId(UUID.randomUUID());
        appGroup.setName("Name");
        appGroup.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setUpdatedBy("2020-03-01");
        appGroup.setVersion(1);

        AppGroup appGroup2 = new AppGroup();
        appGroup2.setAccounts(new ArrayList<>());
        appGroup2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup2.setId(UUID.randomUUID());
        appGroup2.setName("Name");
        appGroup2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup2.setUpdatedBy("2020-03-01");
        appGroup2.setVersion(1);
        AppGroup appGroup3 = mock(AppGroup.class);
        when(appGroup3.setAccounts(Mockito.<List<Account>>any())).thenReturn(appGroup);
        when(appGroup3.setName(Mockito.<String>any())).thenReturn(appGroup2);
        doNothing().when(appGroup3).setCreatedAt(Mockito.<OffsetDateTime>any());
        doNothing().when(appGroup3).setCreatedBy(Mockito.<String>any());
        doNothing().when(appGroup3).setId(Mockito.<UUID>any());
        doNothing().when(appGroup3).setUpdatedAt(Mockito.<OffsetDateTime>any());
        doNothing().when(appGroup3).setUpdatedBy(Mockito.<String>any());
        doNothing().when(appGroup3).setVersion(Mockito.<Integer>any());
        appGroup3.setAccounts(new ArrayList<>());
        appGroup3.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup3.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup3.setId(UUID.randomUUID());
        appGroup3.setName("Name");
        appGroup3.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup3.setUpdatedBy("2020-03-01");
        appGroup3.setVersion(1);

        // Act
        baseService.preprocessUpdateModel(appGroup3, new GroupRequestDTO());

        // Assert that nothing has changed
        verify(appGroup3).setAccounts(isA(List.class));
        verify(appGroup3).setName(eq("Name"));
        verify(appGroup3).setCreatedAt(isA(OffsetDateTime.class));
        verify(appGroup3).setCreatedBy(eq("Jan 1, 2020 8:00am GMT+0100"));
        verify(appGroup3).setId(isA(UUID.class));
        verify(appGroup3).setUpdatedAt(isA(OffsetDateTime.class));
        verify(appGroup3).setUpdatedBy(eq("2020-03-01"));
        verify(appGroup3).setVersion(eq(1));
    }

    /**
     * Method under test:
     * {@link BaseService#postprocessUpdateModel(BaseModel, BaseDTO)}
     */
    @Test
    void testPostprocessUpdateModel() {
        // Arrange
        AppGroup appGroup = new AppGroup();
        appGroup.setAccounts(new ArrayList<>());
        appGroup.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup.setId(UUID.randomUUID());
        appGroup.setName("Name");
        appGroup.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setUpdatedBy("2020-03-01");
        appGroup.setVersion(1);

        AppGroup appGroup2 = new AppGroup();
        appGroup2.setAccounts(new ArrayList<>());
        appGroup2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup2.setId(UUID.randomUUID());
        appGroup2.setName("Name");
        appGroup2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup2.setUpdatedBy("2020-03-01");
        appGroup2.setVersion(1);
        AppGroup appGroup3 = mock(AppGroup.class);
        when(appGroup3.setAccounts(Mockito.<List<Account>>any())).thenReturn(appGroup);
        when(appGroup3.setName(Mockito.<String>any())).thenReturn(appGroup2);
        doNothing().when(appGroup3).setCreatedAt(Mockito.<OffsetDateTime>any());
        doNothing().when(appGroup3).setCreatedBy(Mockito.<String>any());
        doNothing().when(appGroup3).setId(Mockito.<UUID>any());
        doNothing().when(appGroup3).setUpdatedAt(Mockito.<OffsetDateTime>any());
        doNothing().when(appGroup3).setUpdatedBy(Mockito.<String>any());
        doNothing().when(appGroup3).setVersion(Mockito.<Integer>any());
        appGroup3.setAccounts(new ArrayList<>());
        appGroup3.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup3.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup3.setId(UUID.randomUUID());
        appGroup3.setName("Name");
        appGroup3.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup3.setUpdatedBy("2020-03-01");
        appGroup3.setVersion(1);

        // Act
        baseService.postprocessUpdateModel(appGroup3, new GroupRequestDTO());

        // Assert that nothing has changed
        verify(appGroup3).setAccounts(isA(List.class));
        verify(appGroup3).setName(eq("Name"));
        verify(appGroup3).setCreatedAt(isA(OffsetDateTime.class));
        verify(appGroup3).setCreatedBy(eq("Jan 1, 2020 8:00am GMT+0100"));
        verify(appGroup3).setId(isA(UUID.class));
        verify(appGroup3).setUpdatedAt(isA(OffsetDateTime.class));
        verify(appGroup3).setUpdatedBy(eq("2020-03-01"));
        verify(appGroup3).setVersion(eq(1));
    }

    /**
     * Method under test: {@link BaseService#postprocessModel(BaseModel, BaseDTO)}
     */
    @Test
    void testPostprocessModel() {
        // Arrange
        AppGroup appGroup = new AppGroup();
        appGroup.setAccounts(new ArrayList<>());
        appGroup.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup.setId(UUID.randomUUID());
        appGroup.setName("Name");
        appGroup.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setUpdatedBy("2020-03-01");
        appGroup.setVersion(1);

        AppGroup appGroup2 = new AppGroup();
        appGroup2.setAccounts(new ArrayList<>());
        appGroup2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup2.setId(UUID.randomUUID());
        appGroup2.setName("Name");
        appGroup2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup2.setUpdatedBy("2020-03-01");
        appGroup2.setVersion(1);
        AppGroup appGroup3 = mock(AppGroup.class);
        when(appGroup3.setAccounts(Mockito.<List<Account>>any())).thenReturn(appGroup);
        when(appGroup3.setName(Mockito.<String>any())).thenReturn(appGroup2);
        doNothing().when(appGroup3).setCreatedAt(Mockito.<OffsetDateTime>any());
        doNothing().when(appGroup3).setCreatedBy(Mockito.<String>any());
        doNothing().when(appGroup3).setId(Mockito.<UUID>any());
        doNothing().when(appGroup3).setUpdatedAt(Mockito.<OffsetDateTime>any());
        doNothing().when(appGroup3).setUpdatedBy(Mockito.<String>any());
        doNothing().when(appGroup3).setVersion(Mockito.<Integer>any());
        appGroup3.setAccounts(new ArrayList<>());
        appGroup3.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup3.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup3.setId(UUID.randomUUID());
        appGroup3.setName("Name");
        appGroup3.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup3.setUpdatedBy("2020-03-01");
        appGroup3.setVersion(1);

        // Act
        baseService.postprocessModel(appGroup3, new GroupRequestDTO());

        // Assert that nothing has changed
        verify(appGroup3).setAccounts(isA(List.class));
        verify(appGroup3).setName(eq("Name"));
        verify(appGroup3).setCreatedAt(isA(OffsetDateTime.class));
        verify(appGroup3).setCreatedBy(eq("Jan 1, 2020 8:00am GMT+0100"));
        verify(appGroup3).setId(isA(UUID.class));
        verify(appGroup3).setUpdatedAt(isA(OffsetDateTime.class));
        verify(appGroup3).setUpdatedBy(eq("2020-03-01"));
        verify(appGroup3).setVersion(eq(1));
    }

    /**
     * Method under test: {@link BaseService#deleteById(UUID)}
     */
    @Test
    void testDeleteById() {
        // Arrange
        AppGroup appGroup = new AppGroup();
        appGroup.setAccounts(new ArrayList<>());
        appGroup.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup.setId(UUID.randomUUID());
        appGroup.setName("Name");
        appGroup.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setUpdatedBy("2020-03-01");
        appGroup.setVersion(1);
        Optional<AppGroup> ofResult = Optional.of(appGroup);
        doNothing().when(groupRepository).delete(Mockito.<AppGroup>any());
        when(groupRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);

        // Act
        baseService.deleteById(UUID.randomUUID());

        // Assert that nothing has changed
        verify(groupRepository).delete(isA(AppGroup.class));
        verify(groupRepository).findById(isA(UUID.class));
    }

    /**
     * Method under test: {@link BaseService#deleteById(UUID)}
     */
    @Test
    void testDeleteById2() {
        // Arrange
        Optional<AppGroup> emptyResult = Optional.empty();
        when(groupRepository.findById(Mockito.<UUID>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> baseService.deleteById(UUID.randomUUID()));
        verify(groupRepository).findById(isA(UUID.class));
    }

    /**
     * Method under test: {@link BaseService#deleteById(UUID)}
     */
    @Test
    void testDeleteById3() {
        // Arrange
        AppGroup appGroup = new AppGroup();
        appGroup.setAccounts(new ArrayList<>());
        appGroup.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup.setId(UUID.randomUUID());
        appGroup.setName("Name");
        appGroup.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setUpdatedBy("2020-03-01");
        appGroup.setVersion(1);
        Optional<AppGroup> ofResult = Optional.of(appGroup);
        Class<Object> clazz = Object.class;
        doThrow(new NotFoundException(clazz)).when(groupRepository).delete(Mockito.<AppGroup>any());
        when(groupRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> baseService.deleteById(UUID.randomUUID()));
        verify(groupRepository).delete(isA(AppGroup.class));
        verify(groupRepository).findById(isA(UUID.class));
    }

    /**
     * Method under test: {@link BaseService#find(UUID, boolean)}
     */
    @Test
    void testFind() {
        // Arrange
        AppGroup appGroup = new AppGroup();
        appGroup.setAccounts(new ArrayList<>());
        appGroup.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        appGroup.setId(UUID.randomUUID());
        appGroup.setName("Name");
        appGroup.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        appGroup.setUpdatedBy("2020-03-01");
        appGroup.setVersion(1);
        Optional<AppGroup> ofResult = Optional.of(appGroup);
        when(groupRepository.findById(Mockito.<UUID>any())).thenReturn(ofResult);

        // Act
        AppGroup actualFindResult = baseService.find(UUID.randomUUID(), true);

        // Assert
        verify(groupRepository).findById(isA(UUID.class));
        assertSame(appGroup, actualFindResult);
    }

    /**
     * Method under test: {@link BaseService#find(UUID, boolean)}
     */
    @Test
    void testFind2() {
        // Arrange
        Optional<AppGroup> emptyResult = Optional.empty();
        when(groupRepository.findById(Mockito.<UUID>any())).thenReturn(emptyResult);

        // Act
        AppGroup actualFindResult = baseService.find(UUID.randomUUID(), true);

        // Assert
        verify(groupRepository).findById(isA(UUID.class));
        assertNull(actualFindResult);
    }

    /**
     * Method under test: {@link BaseService#find(UUID, boolean)}
     */
    @Test
    void testFind3() {
        // Arrange
        Class<Object> clazz = Object.class;
        when(groupRepository.findById(Mockito.<UUID>any())).thenThrow(new NotFoundException(clazz));

        // Act and Assert
        assertThrows(NotFoundException.class, () -> baseService.find(UUID.randomUUID(), true));
        verify(groupRepository).findById(isA(UUID.class));
    }

    /**
     * Method under test: {@link BaseService#find(UUID, boolean)}
     */
    @Test
    void testFind4() {
        // Arrange
        Optional<AppGroup> emptyResult = Optional.empty();
        when(groupRepository.findById(Mockito.<UUID>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(NotFoundException.class, () -> baseService.find(UUID.randomUUID(), false));
        verify(groupRepository).findById(isA(UUID.class));
    }

    /**
     * Method under test: {@link BaseService#getList(String[], Pageable)}
     */
    @Test
    void testGetList() {
        // Arrange
        when(groupRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        ArrayList<BaseDTO> baseDTOList = new ArrayList<>();
        when(mappingUtils.mapListToDTO(Mockito.<List<Object>>any(), Mockito.<Class<BaseDTO>>any())).thenReturn(baseDTOList);

        // Act
        ApiPageableResponse actualList = baseService.getList(new String[]{}, QPageRequest.of(10, 3));

        // Assert
        verify(groupRepository).findAll(isA(Pageable.class));
        verify(mappingUtils).mapListToDTO(isA(List.class), isA(Class.class));
        assertEquals(0, actualList.getPageSize());
        assertEquals(0L, actualList.getTotalElements());
        assertEquals(1, actualList.getCurrentPage());
        assertEquals(1, actualList.getTotalPages());
        List<?> data = actualList.getData();
        assertTrue(data.isEmpty());
        assertTrue(actualList.isFirst());
        assertTrue(actualList.isLast());
        assertSame(baseDTOList, data);
    }

    /**
     * Method under test: {@link BaseService#getList(String[], Pageable)}
     */
    @Test
    void testGetList2() {
        // Arrange
        when(groupRepository.findAll(Mockito.<Pageable>any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        ArrayList<BaseDTO> baseDTOList = new ArrayList<>();
        when(mappingUtils.mapListToDTO(Mockito.<List<Object>>any(), Mockito.<Class<BaseDTO>>any())).thenReturn(baseDTOList);

        // Act
        ApiPageableResponse actualList = baseService.getList(null, QPageRequest.of(10, 3));

        // Assert
        verify(groupRepository).findAll(isA(Pageable.class));
        verify(mappingUtils).mapListToDTO(isA(List.class), isA(Class.class));
        assertEquals(0, actualList.getPageSize());
        assertEquals(0L, actualList.getTotalElements());
        assertEquals(1, actualList.getCurrentPage());
        assertEquals(1, actualList.getTotalPages());
        List<?> data = actualList.getData();
        assertTrue(data.isEmpty());
        assertTrue(actualList.isFirst());
        assertTrue(actualList.isLast());
        assertSame(baseDTOList, data);
    }

    /**
     * Method under test: {@link BaseService#formatPagingResponse(Page)}
     */
    @Test
    void testFormatPagingResponse() {
        // Arrange
        ArrayList<BaseDTO> baseDTOList = new ArrayList<>();
        when(mappingUtils.mapListToDTO(Mockito.<List<Object>>any(), Mockito.<Class<BaseDTO>>any())).thenReturn(baseDTOList);

        // Act
        ApiPageableResponse actualFormatPagingResponseResult = baseService
                .formatPagingResponse(new PageImpl<>(new ArrayList<>()));

        // Assert
        verify(mappingUtils).mapListToDTO(isA(List.class), isA(Class.class));
        assertEquals(0, actualFormatPagingResponseResult.getPageSize());
        assertEquals(0L, actualFormatPagingResponseResult.getTotalElements());
        assertEquals(1, actualFormatPagingResponseResult.getCurrentPage());
        assertEquals(1, actualFormatPagingResponseResult.getTotalPages());
        List<?> data = actualFormatPagingResponseResult.getData();
        assertTrue(data.isEmpty());
        assertTrue(actualFormatPagingResponseResult.isFirst());
        assertTrue(actualFormatPagingResponseResult.isLast());
        assertSame(baseDTOList, data);
    }

    /**
     * Method under test: {@link BaseService#formatPagingResponse(Page)}
     */
    @Test
    void testFormatPagingResponse2() {
        // Arrange
        Class<Object> clazz = Object.class;
        when(mappingUtils.mapListToDTO(Mockito.<List<Object>>any(), Mockito.<Class<BaseDTO>>any()))
                .thenThrow(new NotFoundException(clazz));

        // Act and Assert
        assertThrows(NotFoundException.class, () -> baseService.formatPagingResponse(new PageImpl<>(new ArrayList<>())));
        verify(mappingUtils).mapListToDTO(isA(List.class), isA(Class.class));
    }
}
