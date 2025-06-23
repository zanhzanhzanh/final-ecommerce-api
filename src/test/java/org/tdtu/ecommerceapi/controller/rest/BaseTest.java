package org.tdtu.ecommerceapi.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.Mockito;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.tdtu.ecommerceapi.dto.BaseDTO;
import org.tdtu.ecommerceapi.dto.api.ApiPageableResponse;
import org.tdtu.ecommerceapi.model.BaseModel;
import org.tdtu.ecommerceapi.service.BaseService;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public abstract class BaseTest<
        T,
        S extends BaseService<M, REQ, RES, ?>,
        M extends BaseModel,
        REQ extends BaseDTO,
        RES extends BaseDTO> {

    protected MockMvc mockMvc;
    protected ObjectMapper objectMapper = new ObjectMapper();
    protected T controller;
    protected S service;
    protected String endpoint;
    protected final Class<REQ> requestDtoClass = (Class<REQ>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[3];
    protected final Class<RES> responseDtoClass = (Class<RES>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[4];

    protected void setupController(T controller, S service, String endpoint) {
        this.controller = controller;
        this.service = service;
        this.endpoint = endpoint;
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    protected void testGetById() throws Exception {
        // Arrange
        RES responseDto = createMockResponse(responseDtoClass);

        Mockito.when(service.getById(Mockito.any(UUID.class), Mockito.anyBoolean())).thenReturn(responseDto);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(endpoint + "/{id}", UUID.randomUUID());

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(responseDto)));
    }

    protected void testCreate() throws Exception {
        // Arrange
        REQ requestDto = createMockRequest(requestDtoClass);
        RES responseDto = createMockResponse(responseDtoClass);

        Mockito.when(service.create(Mockito.any(requestDtoClass))).thenReturn(responseDto);

        String content = objectMapper.writeValueAsString(requestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(endpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(responseDto)));
    }

    protected void testUpdateById() throws Exception {
        // Arrange
        REQ requestDto = createMockRequest(requestDtoClass);
        RES responseDto = createMockResponse(responseDtoClass);

        Mockito.when(service.updateById(Mockito.any(UUID.class), Mockito.any(requestDtoClass))).thenReturn(responseDto);

        String content = objectMapper.writeValueAsString(requestDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put(endpoint + "/{id}", UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(responseDto)));
    }

    protected void testDeleteById() throws Exception {
        // Arrange
        Mockito.doNothing().when(service).deleteById(Mockito.any(UUID.class));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(endpoint + "/{id}", UUID.randomUUID());

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    protected void testDeleteByIdWithInvalidContentType() throws Exception {
        // Arrange
        Mockito.doNothing().when(service).deleteById(Mockito.any(UUID.class));

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete(endpoint + "/{id}", UUID.randomUUID())
                .contentType("https://example.org/example");

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    protected void testGetList() throws Exception {
        // Arrange
        ApiPageableResponse buildResult = ApiPageableResponse.builder()
                .currentPage(1)
                .data(new ArrayList<>())
                .pageSize(3)
                .totalElements(1L)
                .totalPages(1)
                .build();
        Mockito.when(service.getList(Mockito.any(), Mockito.any())).thenReturn(buildResult);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(endpoint)
                .param("page", "10")
                .param("size", "3")
                .param("filter", "Filter");

        // Act and Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.pageSize").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    protected void testDirectGetList(Pageable pageable, String[] filters, ApiPageableResponse expectedResponse) throws Exception {
        //   Diffblue Cover was unable to create a Spring-specific test for this Spring method.

        // Arrange
        Mockito.when(service.getList(Mockito.any(String[].class), Mockito.any(Pageable.class))).thenReturn(expectedResponse);

        java.lang.reflect.Method getListMethod = controller.getClass().getMethod("getList", Pageable.class, String[].class);
        ResponseEntity<?> actualList = (ResponseEntity<?>) getListMethod.invoke(controller, pageable, filters);

        // Assert
        assertEquals(HttpStatus.OK, actualList.getStatusCode());
        assertTrue(actualList.getBody() instanceof ApiPageableResponse);
        ApiPageableResponse response = (ApiPageableResponse) actualList.getBody();
        assertEquals(expectedResponse.getCurrentPage(), response.getCurrentPage());
        assertEquals(expectedResponse.getTotalPages(), response.getTotalPages());
        assertEquals(expectedResponse.getTotalElements(), response.getTotalElements());
        assertEquals(expectedResponse.getPageSize(), response.getPageSize());
        assertEquals(expectedResponse.getData(), response.getData());
        assertFalse(response.isFirst());
        assertFalse(response.isLast());
        assertTrue(actualList.hasBody());
        assertTrue(actualList.getHeaders().isEmpty());
    }

    protected <D> D createMockResponse(Class<D> responseType) throws Exception {
        return responseType.getDeclaredConstructor().newInstance();
    }

    protected <R> R createMockRequest(Class<R> requestType) throws Exception {
        R request = requestType.getDeclaredConstructor().newInstance();
        if (requestType.getDeclaredFields().length > 0) {
            for (java.lang.reflect.Field field : requestType.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getType().equals(UUID.class)) {
                    field.set(request, UUID.randomUUID());
                } else if (field.getType().equals(String.class)) {
                    field.set(request, "Sample " + field.getName());
                }
            }
        }
        return request;
    }
}