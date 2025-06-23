package org.tdtu.ecommerceapi.service.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tdtu.ecommerceapi.dto.BaseDTO;
import org.tdtu.ecommerceapi.dto.rest.request.AddressReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.AddressResDto;
import org.tdtu.ecommerceapi.model.Account;
import org.tdtu.ecommerceapi.model.AppGroup;
import org.tdtu.ecommerceapi.model.GoogleAccount;
import org.tdtu.ecommerceapi.model.rest.Address;
import org.tdtu.ecommerceapi.repository.AddressRepository;
import org.tdtu.ecommerceapi.service.AccountService;
import org.tdtu.ecommerceapi.utils.MappingUtils;

@ContextConfiguration(classes = {AddressService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AddressServiceTest {
    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private AddressRepository addressRepository;

    @Autowired
    private AddressService addressService;

    @MockitoBean
    private MappingUtils mappingUtils;

    /**
     * Method under test:
     * {@link AddressService#postprocessModel(Address, AddressReqDto)}
     */
    @Test
    void testPostprocessModel() {
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
        OffsetDateTime createdAt = OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);
        googleAccount.setCreatedAt(createdAt);
        googleAccount.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        googleAccount.setEmail("jane.doe@example.org");
        googleAccount.setEmail_verified("jane.doe@example.org");
        googleAccount.setFamily_name("Family name");
        googleAccount.setGiven_name("Given name");
        UUID id = UUID.randomUUID();
        googleAccount.setId(id);
        googleAccount.setName("Name");
        googleAccount.setPicture("Picture");
        googleAccount.setSub("Sub");
        OffsetDateTime updatedAt = OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);
        googleAccount.setUpdatedAt(updatedAt);
        googleAccount.setUpdatedBy("2020-03-01");
        googleAccount.setVersion(1);

        AppGroup group = new AppGroup();
        ArrayList<Account> accounts = new ArrayList<>();
        group.setAccounts(accounts);
        OffsetDateTime createdAt2 = OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);
        group.setCreatedAt(createdAt2);
        group.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        UUID id2 = UUID.randomUUID();
        group.setId(id2);
        group.setName("Name");
        OffsetDateTime updatedAt2 = OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC);
        group.setUpdatedAt(updatedAt2);
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
        googleAccount2.setAccount(account2);
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
        when(accountService.find(Mockito.<UUID>any(), anyBoolean())).thenReturn(account3);

        Account account4 = new Account();
        account4.setAddresses(new ArrayList<>());
        account4.setBirthYear(1);
        account4.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account4.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        account4.setEmail("jane.doe@example.org");
        account4.setGoogleAccount(new GoogleAccount());
        account4.setGroup(new AppGroup());
        account4.setId(UUID.randomUUID());
        account4.setPassword("iloveyou");
        account4.setPhoneNumber("6625550144");
        account4.setPromotionIds(new HashSet<>());
        account4.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account4.setUpdatedBy("2020-03-01");
        account4.setUsername("janedoe");
        account4.setVersion(1);

        GoogleAccount googleAccount3 = new GoogleAccount();
        googleAccount3.setAccount(account4);
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

        Account account5 = new Account();
        account5.setAddresses(new ArrayList<>());
        account5.setBirthYear(1);
        account5.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account5.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        account5.setEmail("jane.doe@example.org");
        account5.setGoogleAccount(googleAccount3);
        account5.setGroup(group3);
        account5.setId(UUID.randomUUID());
        account5.setPassword("iloveyou");
        account5.setPhoneNumber("6625550144");
        account5.setPromotionIds(new HashSet<>());
        account5.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        account5.setUpdatedBy("2020-03-01");
        account5.setUsername("janedoe");
        account5.setVersion(1);

        Address model = new Address();
        model.setAccount(account5);
        model.setBuildingName("Building Name");
        model.setCity("Oxford");
        model.setCountry("GB");
        model.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        model.setId(UUID.randomUUID());
        model.setPincode("Pincode");
        model.setState("MD");
        model.setStreet("Street");
        model.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setUpdatedBy("2020-03-01");
        model.setVersion(1);

        // Act
        addressService.postprocessModel(model, new AddressReqDto());

        // Assert
        verify(accountService).find(isNull(), eq(false));
        Account account6 = model.getAccount().getGoogleAccount().getAccount();
        Collection<? extends GrantedAuthority> authorities = account6.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities instanceof Set);
        GoogleAccount googleAccount4 = account6.getGoogleAccount();
        assertEquals("2020-03-01", googleAccount4.getUpdatedBy());
        AppGroup group4 = account6.getGroup();
        assertEquals("2020-03-01", group4.getUpdatedBy());
        assertEquals("Family name", googleAccount4.getFamily_name());
        assertEquals("Given name", googleAccount4.getGiven_name());
        assertEquals("Jan 1, 2020 8:00am GMT+0100", googleAccount4.getCreatedBy());
        assertEquals("Jan 1, 2020 8:00am GMT+0100", group4.getCreatedBy());
        assertEquals("Name", group4.getName());
        assertEquals("Name", googleAccount4.getName());
        assertEquals("Picture", googleAccount4.getPicture());
        assertEquals("Sub", googleAccount4.getSub());
        assertEquals("jane.doe@example.org", googleAccount4.getEmail());
        assertEquals("jane.doe@example.org", googleAccount4.getEmail_verified());
        assertEquals(1, googleAccount4.getVersion().intValue());
        assertEquals(1, group4.getVersion().intValue());
        List<Account> accounts2 = group4.getAccounts();
        assertTrue(accounts2.isEmpty());
        assertSame(accounts, accounts2);
        assertSame(account, googleAccount4.getAccount());
        assertSame(createdAt, googleAccount4.getCreatedAt());
        assertSame(createdAt2, group4.getCreatedAt());
        assertSame(updatedAt, googleAccount4.getUpdatedAt());
        assertSame(updatedAt2, group4.getUpdatedAt());
        assertSame(id, googleAccount4.getId());
        assertSame(id2, group4.getId());
    }

    /**
     * Method under test:
     * {@link AddressService#postprocessUpdateModel(Address, AddressReqDto)}
     */
    @Test
    void testPostprocessUpdateModel() {
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
        Address model = mock(Address.class);
        doNothing().when(model).setCreatedAt(Mockito.<OffsetDateTime>any());
        doNothing().when(model).setCreatedBy(Mockito.<String>any());
        doNothing().when(model).setId(Mockito.<UUID>any());
        doNothing().when(model).setUpdatedAt(Mockito.<OffsetDateTime>any());
        doNothing().when(model).setUpdatedBy(Mockito.<String>any());
        doNothing().when(model).setVersion(Mockito.<Integer>any());
        doNothing().when(model).setAccount(Mockito.<Account>any());
        doNothing().when(model).setBuildingName(Mockito.<String>any());
        doNothing().when(model).setCity(Mockito.<String>any());
        doNothing().when(model).setCountry(Mockito.<String>any());
        doNothing().when(model).setPincode(Mockito.<String>any());
        doNothing().when(model).setState(Mockito.<String>any());
        doNothing().when(model).setStreet(Mockito.<String>any());
        model.setAccount(account2);
        model.setBuildingName("Building Name");
        model.setCity("Oxford");
        model.setCountry("GB");
        model.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        model.setId(UUID.randomUUID());
        model.setPincode("Pincode");
        model.setState("MD");
        model.setStreet("Street");
        model.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setUpdatedBy("2020-03-01");
        model.setVersion(1);

        // Act
        addressService.postprocessUpdateModel(model, new AddressReqDto());

        // Assert that nothing has changed
        verify(model).setCreatedAt(isA(OffsetDateTime.class));
        verify(model).setCreatedBy(eq("Jan 1, 2020 8:00am GMT+0100"));
        verify(model).setId(isA(UUID.class));
        verify(model).setUpdatedAt(isA(OffsetDateTime.class));
        verify(model).setUpdatedBy(eq("2020-03-01"));
        verify(model).setVersion(eq(1));
        verify(model).setAccount(isA(Account.class));
        verify(model).setBuildingName(eq("Building Name"));
        verify(model).setCity(eq("Oxford"));
        verify(model).setCountry(eq("GB"));
        verify(model).setPincode(eq("Pincode"));
        verify(model).setState(eq("MD"));
        verify(model).setStreet(eq("Street"));
    }

    /**
     * Method under test: {@link AddressService#findByAccount(UUID)}
     */
    @Test
    void testFindByAccount() {
        // Arrange
        when(addressRepository.findByAccount(Mockito.<UUID>any())).thenReturn(new ArrayList<>());
        ArrayList<BaseDTO> baseDTOList = new ArrayList<>();
        when(mappingUtils.mapListToDTO(Mockito.<List<Object>>any(), Mockito.<Class<BaseDTO>>any())).thenReturn(baseDTOList);

        // Act
        List<AddressResDto> actualFindByAccountResult = addressService.findByAccount(UUID.randomUUID());

        // Assert
        verify(addressRepository).findByAccount(isA(UUID.class));
        verify(mappingUtils).mapListToDTO(isA(List.class), isA(Class.class));
        assertTrue(actualFindByAccountResult.isEmpty());
        assertSame(baseDTOList, actualFindByAccountResult);
    }
}
