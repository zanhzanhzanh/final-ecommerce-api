//package org.tdtu.ecommerceapi.controller.rest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertSame;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.Mockito.anyBoolean;
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import java.util.ArrayList;
//
//import java.util.UUID;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.aot.DisabledInAotMode;
//import org.springframework.test.context.bean.override.mockito.MockitoBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.tdtu.ecommerceapi.dto.api.ApiPageableResponse;
//import org.tdtu.ecommerceapi.dto.rest.request.AddressReqDto;
//import org.tdtu.ecommerceapi.dto.rest.response.AddressResDto;
//import org.tdtu.ecommerceapi.service.rest.AddressService;
//
//@ContextConfiguration(classes = {AddressController.class})
//@ExtendWith(SpringExtension.class)
//@DisabledInAotMode
//class AddressRawFile {
//    @Autowired
//    private AddressController addressController;
//
//    @MockitoBean
//    private AddressService addressService;
//
//    /**
//     * Method under test: {@link AddressController#getById(UUID)}
//     */
//    @Test
//    void testGetById() throws Exception {
//        // Arrange
//        when(addressService.getById(Mockito.<UUID>any(), anyBoolean())).thenReturn(new AddressResDto());
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/addresses/{id}", UUID.randomUUID());
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(addressController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.content()
//                        .string(
//                                "{\"street\":null,\"buildingName\":null,\"city\":null,\"country\":null,\"state\":null,\"pincode\":null,\"accountId"
//                                        + "\":null,\"id\":null}"));
//    }
//
//    /**
//     * Method under test: {@link AddressController#create(AddressReqDto)}
//     */
//    @Test
//    void testCreate() throws Exception {
//        // Arrange
//        when(addressService.create(Mockito.<AddressReqDto>any())).thenReturn(new AddressResDto());
//
//        AddressReqDto addressReqDto = new AddressReqDto();
//        addressReqDto.setAccountId(UUID.randomUUID());
//        addressReqDto.setBuildingName("Building Name");
//        addressReqDto.setCity("Oxford");
//        addressReqDto.setCountry("GB");
//        addressReqDto.setPincode("Pincode");
//        addressReqDto.setState("MD");
//        addressReqDto.setStreet("Street");
//        String content = (new ObjectMapper()).writeValueAsString(addressReqDto);
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/addresses")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content);
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(addressController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.content()
//                        .string(
//                                "{\"street\":null,\"buildingName\":null,\"city\":null,\"country\":null,\"state\":null,\"pincode\":null,\"accountId"
//                                        + "\":null,\"id\":null}"));
//    }
//
//    /**
//     * Method under test: {@link AddressController#updateById(UUID, AddressReqDto)}
//     */
//    @Test
//    void testUpdateById() throws Exception {
//        // Arrange
//        when(addressService.updateById(Mockito.<UUID>any(), Mockito.<AddressReqDto>any())).thenReturn(new AddressResDto());
//
//        AddressReqDto addressReqDto = new AddressReqDto();
//        addressReqDto.setAccountId(UUID.randomUUID());
//        addressReqDto.setBuildingName("Building Name");
//        addressReqDto.setCity("Oxford");
//        addressReqDto.setCountry("GB");
//        addressReqDto.setPincode("Pincode");
//        addressReqDto.setState("MD");
//        addressReqDto.setStreet("Street");
//        String content = (new ObjectMapper()).writeValueAsString(addressReqDto);
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/v1/addresses/{id}", UUID.randomUUID())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(content);
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(addressController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.content()
//                        .string(
//                                "{\"street\":null,\"buildingName\":null,\"city\":null,\"country\":null,\"state\":null,\"pincode\":null,\"accountId"
//                                        + "\":null,\"id\":null}"));
//    }
//
//    /**
//     * Method under test: {@link AddressController#deleteById(UUID)}
//     */
//    @Test
//    void testDeleteById() throws Exception {
//        // Arrange
//        doNothing().when(addressService).deleteById(Mockito.<UUID>any());
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/v1/addresses/{id}",
//                UUID.randomUUID());
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(addressController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
//    /**
//     * Method under test: {@link AddressController#deleteById(UUID)}
//     */
//    @Test
//    void testDeleteById2() throws Exception {
//        // Arrange
//        doNothing().when(addressService).deleteById(Mockito.<UUID>any());
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/v1/addresses/{id}",
//                UUID.randomUUID());
//        requestBuilder.contentType("https://example.org/example");
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(addressController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//
////    /**
////     * Method under test: {@link AddressController#getList(Pageable, String[])}
////     */
////    @Test
////    void testGetList() {
////        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.
////
////        // Arrange
////        AddressService addressService = mock(AddressService.class);
////        ApiPageableResponse.ApiPageableResponseBuilder currentPageResult = ApiPageableResponse.builder().currentPage(1);
////        ArrayList<Object> data = new ArrayList<>();
////        ApiPageableResponse buildResult = currentPageResult.data(data).pageSize(3).totalElements(1L).totalPages(1).build();
////        when(addressService.getList(Mockito.<String[]>any(), Mockito.<Pageable>any())).thenReturn(buildResult);
////        AddressController addressController = new AddressController(addressService);
////
////        // Act
////        ResponseEntity<?> actualList = addressController.getList(QPageRequest.of(10, 3), new String[]{"Filter"});
////
////        // Assert
////        verify(addressService).getList(isA(String[].class), isA(Pageable.class));
////        HttpStatusCode statusCode = actualList.getStatusCode();
////        assertTrue(statusCode instanceof HttpStatus);
////        Object body = actualList.getBody();
////        assertTrue(body instanceof ApiPageableResponse);
////        assertEquals(1, ((ApiPageableResponse) body).getCurrentPage());
////        assertEquals(1, ((ApiPageableResponse) body).getTotalPages());
////        assertEquals(1L, ((ApiPageableResponse) body).getTotalElements());
////        assertEquals(200, actualList.getStatusCodeValue());
////        assertEquals(3, ((ApiPageableResponse) body).getPageSize());
////        assertEquals(HttpStatus.OK, statusCode);
////        assertFalse(((ApiPageableResponse) body).isFirst());
////        assertFalse(((ApiPageableResponse) body).isLast());
////        List<?> data2 = ((ApiPageableResponse) body).getData();
////        assertTrue(data2.isEmpty());
////        assertTrue(actualList.hasBody());
////        assertTrue(actualList.getHeaders().isEmpty());
////        assertSame(data, data2);
////    }
//
//    /**
//     * Method under test: {@link AddressController#getList(Pageable, String[])}
//     */
//    @Test
//    void testGetList() throws Exception {
//        // Arrange
//        ApiPageableResponse.ApiPageableResponseBuilder currentPageResult = ApiPageableResponse.builder().currentPage(1);
//        ArrayList<Object> data = new ArrayList<>();
//        ApiPageableResponse buildResult = currentPageResult.data(data).pageSize(3).totalElements(1L).totalPages(1).build();
//        when(addressService.getList(Mockito.<String[]>any(), Mockito.<Pageable>any())).thenReturn(buildResult);
//
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/addresses")
//                .param("page", "10")
//                .param("size", "3")
//                .param("filter", "Filter");
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(addressController)
//                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage").value(1))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(1))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.pageSize").value(3))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
//    }
//
//    /**
//     * Method under test: {@link AddressController#getByAccountId(UUID)}
//     */
//    @Test
//    void testGetByAccountId() throws Exception {
//        // Arrange
//        when(addressService.findByAccount(Mockito.<UUID>any())).thenReturn(new ArrayList<>());
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
//                .get("/v1/addresses/get-by-account/{accountId}", UUID.randomUUID());
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(addressController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.content().string("[]"));
//    }
//
//    /**
//     * Method under test: {@link AddressController#getByAccountId(UUID)}
//     */
//    @Test
//    void testGetByAccountId2() throws Exception {
//        // Arrange
//        when(addressService.findByAccount(Mockito.<UUID>any())).thenReturn(new ArrayList<>());
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
//                .get("/v1/addresses/get-by-account/{accountId}", UUID.randomUUID());
//        requestBuilder.contentType("https://example.org/example");
//
//        // Act and Assert
//        MockMvcBuilders.standaloneSetup(addressController)
//                .build()
//                .perform(requestBuilder)
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.content().string("[]"));
//    }
//}
