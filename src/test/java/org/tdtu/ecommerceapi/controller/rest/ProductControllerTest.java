package org.tdtu.ecommerceapi.controller.rest;

import static org.mockito.Mockito.doNothing;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.tdtu.ecommerceapi.dto.rest.request.ProductReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.ProductResDto;
import org.tdtu.ecommerceapi.model.rest.Product;
import org.tdtu.ecommerceapi.service.provider.OpenAIService;
import org.tdtu.ecommerceapi.service.rest.ProductService;

@ContextConfiguration(classes = {ProductController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ProductControllerTest extends BaseTest<
        ProductController,
        ProductService,
        Product,
        ProductReqDto,
        ProductResDto> {
    @MockitoBean
    private OpenAIService openAIService;

    @Autowired
    private ProductController productController;

    @MockitoBean
    private ProductService productService;

    @BeforeEach
    void setUp() {
        String endpoint = "/v1/products";
        setupController(
                productController,
                productService,
                endpoint);
    }

    /**
     * Method under test: {@link ProductController#getById(UUID)}
     */
    @Test
    void testGetByIdMethod() throws Exception {
        testGetById();
    }

    /**
     * Method under test: {@link ProductController#create(ProductReqDto)}
     */
    @Test
    void testCreateMethod() throws Exception {
        testCreate();
    }

    /**
     * Method under test: {@link ProductController#create(ProductReqDto)}
     */
    @Test
    void testCreateNotFound() throws Exception {
        // Arrange
        when(productService.create(Mockito.<ProductReqDto>any())).thenReturn(new ProductResDto());

        ProductReqDto productReqDto = new ProductReqDto();
        productReqDto.setCategoryId(null);
        productReqDto.setDescription("The characteristics of someone or something");
        productReqDto.setImage("Image");
        productReqDto.setPrice(10.0d);
        productReqDto.setProductName("Product Name");
        productReqDto.setQuantity(1);
        String content = (new ObjectMapper()).writeValueAsString(productReqDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link ProductController#update(UUID, ProductReqDto)}
     */
    @Test
    void testUpdateByIdMethod() throws Exception {
        testUpdateById();
    }

    /**
     * Method under test: {@link ProductController#delete(UUID)}
     */
    @Test
    void testDeleteByIdMethod() throws Exception {
        testDeleteById();
    }

    /**
     * Method under test: {@link ProductController#delete(UUID)}
     */
    @Test
    void testDeleteNotFound() throws Exception {
        // Arrange
        doNothing().when(productService).deleteById(Mockito.<UUID>any());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/v1/products/{id}", "Uri Variables",
                "Uri Variables");

        // Act
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder);

        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    /**
     * Method under test: {@link ProductController#delete(UUID)}
     */
    @Test
    void testDeleteByIdWithInvalidContentTypeMethod() throws Exception {
        testDeleteByIdWithInvalidContentType();
    }

    /**
     * Method under test: {@link ProductController#getList(Pageable, String[])}
     */
    @Test
    void testGetListMethod() throws Exception {
        testGetList();
    }

    /**
     * Method under test: {@link ProductController#findSimilarProducts(UUID)}
     */
    @Test
    void testFindSimilarProducts() throws Exception {
        // Arrange
        when(openAIService.findSimilarProducts(Mockito.<UUID>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/products/similar/{id}",
                UUID.randomUUID());

        // Act and Assert
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }

    /**
     * Method under test: {@link ProductController#findSimilarProducts(UUID)}
     */
    @Test
    void testFindSimilarProductsWithInvalidContentType() throws Exception {
        // Arrange
        when(openAIService.findSimilarProducts(Mockito.<UUID>any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/v1/products/similar/{id}",
                UUID.randomUUID());
        requestBuilder.contentType("https://example.org/example");

        // Act and Assert
        MockMvcBuilders.standaloneSetup(productController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content().string("[]"));
    }
}
