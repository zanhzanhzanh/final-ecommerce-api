package org.tdtu.ecommerceapi.controller.rest;

import static org.mockito.Mockito.when;

import java.util.ArrayList;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.tdtu.ecommerceapi.dto.api.ApiPageableResponse;
import org.tdtu.ecommerceapi.dto.rest.request.AddressReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.AddressResDto;
import org.tdtu.ecommerceapi.model.rest.Address;
import org.tdtu.ecommerceapi.service.rest.AddressService;

@ContextConfiguration(classes = {AddressController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class AddressControllerTest extends BaseTest<
        AddressController,
        AddressService,
        Address,
        AddressReqDto,
        AddressResDto> {
    @Autowired
    private AddressController addressController;

    @MockitoBean
    private AddressService addressService;

    @BeforeEach
    void setUp() {
        String endpoint = "/v1/addresses";
        setupController(
                addressController,
                addressService,
                endpoint);
    }

    /**
     * Method under test: {@link AddressController#getById(UUID)}
     */
    @Test
    void testGetByIdMethod() throws Exception {
        testGetById();
    }

    /**
     * Method under test: {@link AddressController#create(AddressReqDto)}
     */
    @Test
    void testCreateMethod() throws Exception {
        testCreate();
    }

    /**
     * Method under test: {@link AddressController#updateById(UUID, AddressReqDto)}
     */
    @Test
    void testUpdateByIdMethod() throws Exception {
        testUpdateById();
    }

    /**
     * Method under test: {@link AddressController#deleteById(UUID)}
     */
    @Test
    void testDeleteByIdMethod() throws Exception {
        testDeleteById();
    }

    /**
     * Method under test: {@link AddressController#deleteById(UUID)}
     */
    @Test
    void testDeleteByIdWithInvalidContentTypeMethod() throws Exception {
        testDeleteByIdWithInvalidContentType();
    }

    /**
     * Method under test: {@link AddressController#getList(Pageable, String[])}
     */
    @Test
    void testGetListMethod() throws Exception {
        testGetList();
    }

    /**
     * Method under test: {@link AddressController#getList(Pageable, String[])}
     */
    @Test
    void testDirectGetList() throws Exception {
        ApiPageableResponse expectedResponse = ApiPageableResponse.builder()
                .currentPage(1)
                .data(new ArrayList<>())
                .pageSize(3)
                .totalElements(1L)
                .totalPages(1)
                .build();
        Mockito.when(addressService.getList(Mockito.any(), Mockito.any())).thenReturn(expectedResponse);
        testDirectGetList(QPageRequest.of(10, 3), new String[]{"Filter"}, expectedResponse);
    }

    /**
     * Method under test: {@link AddressController#getByAccountId(UUID)}
     */
    @Test
    void testGetByAccountId() throws Exception {
        // Arrange
        when(addressService.findByAccount(Mockito.<UUID>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/v1/addresses/get-by-account/{accountId}", UUID.randomUUID());

        // Act and Assert
        MockMvcBuilders.standaloneSetup(addressController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link AddressController#getByAccountId(UUID)}
     */
    @Test
    void testGetByAccountId2() throws Exception {
        // Arrange
        when(addressService.findByAccount(Mockito.<UUID>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/v1/addresses/get-by-account/{accountId}", UUID.randomUUID());
        requestBuilder.contentType("https://example.org/example");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(addressController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}
