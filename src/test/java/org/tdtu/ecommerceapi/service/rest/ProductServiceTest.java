package org.tdtu.ecommerceapi.service.rest;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tdtu.ecommerceapi.dto.rest.request.ProductReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.ProductResDto;
import org.tdtu.ecommerceapi.model.rest.Category;
import org.tdtu.ecommerceapi.model.rest.Product;
import org.tdtu.ecommerceapi.repository.ProductRepository;
import org.tdtu.ecommerceapi.utils.MappingUtils;

@ContextConfiguration(classes = {ProductService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class ProductServiceTest {
    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private MappingUtils mappingUtils;

    @MockitoBean
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    /**
     * Method under test: {@link ProductService#create(ProductReqDto)}
     */
    @Test
    void testCreate() {
        // Arrange
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
        when(productRepository.save(Mockito.<Product>any())).thenReturn(product);

        Category category2 = new Category();
        category2.setCategoryName("Category Name");
        category2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        category2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        category2.setId(UUID.randomUUID());
        category2.setProducts(new ArrayList<>());
        category2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        category2.setUpdatedBy("2020-03-01");
        category2.setVersion(1);

        Product product2 = new Product();
        product2.setCategory(category2);
        product2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        product2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        product2.setDescription("The characteristics of someone or something");
        product2.setId(UUID.randomUUID());
        product2.setImage("Image");
        product2.setPrice(10.0d);
        product2.setProductName("Product Name");
        product2.setQuantity(1);
        product2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        product2.setUpdatedBy("2020-03-01");
        product2.setVersion(1);
        ProductResDto productResDto = new ProductResDto();
        when(mappingUtils.mapToDTO(Mockito.<Product>any(), Mockito.<Class<ProductResDto>>any())).thenReturn(productResDto);
        when(mappingUtils.mapFromDTO(Mockito.<ProductReqDto>any(), Mockito.<Class<Product>>any())).thenReturn(product2);

        Category category3 = new Category();
        category3.setCategoryName("Category Name");
        category3.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        category3.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        category3.setId(UUID.randomUUID());
        category3.setProducts(new ArrayList<>());
        category3.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        category3.setUpdatedBy("2020-03-01");
        category3.setVersion(1);
        when(categoryService.find(Mockito.<UUID>any(), anyBoolean())).thenReturn(category3);

        // Act
        ProductResDto actualCreateResult = productService.create(new ProductReqDto());

        // Assert
        verify(productRepository).save(isA(Product.class));
        verify(categoryService).find(isNull(), eq(false));
        verify(mappingUtils).mapFromDTO(isA(ProductReqDto.class), isA(Class.class));
        verify(mappingUtils).mapToDTO(isA(Product.class), isA(Class.class));
        assertSame(productResDto, actualCreateResult);
    }

    /**
     * Method under test:
     * {@link ProductService#postprocessUpdateModel(Product, ProductReqDto)}
     */
    @Test
    void testPostprocessUpdateModel() {
        // Arrange
        Category category = new Category();
        category.setCategoryName("Category Name");
        category.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        category.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        category.setId(UUID.randomUUID());
        category.setProducts(new ArrayList<>());
        category.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        category.setUpdatedBy("2020-03-01");
        category.setVersion(1);
        when(categoryService.find(Mockito.<UUID>any(), anyBoolean())).thenReturn(category);

        Category category2 = new Category();
        category2.setCategoryName("Category Name");
        category2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        category2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        category2.setId(UUID.randomUUID());
        category2.setProducts(new ArrayList<>());
        category2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        category2.setUpdatedBy("2020-03-01");
        category2.setVersion(1);

        Product model = new Product();
        model.setCategory(category2);
        model.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        model.setDescription("The characteristics of someone or something");
        model.setId(UUID.randomUUID());
        model.setImage("Image");
        model.setPrice(10.0d);
        model.setProductName("Product Name");
        model.setQuantity(1);
        model.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setUpdatedBy("2020-03-01");
        model.setVersion(1);

        // Act
        productService.postprocessUpdateModel(model, new ProductReqDto());

        // Assert
        verify(categoryService).find(isNull(), eq(false));
    }

    /**
     * Method under test:
     * {@link ProductService#postprocessUpdateModel(Product, ProductReqDto)}
     */
    @Test
    void testPostprocessUpdateModel2() {
        // Arrange
        Category category = new Category();
        category.setCategoryName("Category Name");
        category.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        category.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        category.setId(UUID.randomUUID());
        category.setProducts(new ArrayList<>());
        category.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        category.setUpdatedBy("2020-03-01");
        category.setVersion(1);
        Category category2 = mock(Category.class);
        when(category2.getId()).thenReturn(null);
        doNothing().when(category2).setCreatedAt(Mockito.<OffsetDateTime>any());
        doNothing().when(category2).setCreatedBy(Mockito.<String>any());
        doNothing().when(category2).setId(Mockito.<UUID>any());
        doNothing().when(category2).setUpdatedAt(Mockito.<OffsetDateTime>any());
        doNothing().when(category2).setUpdatedBy(Mockito.<String>any());
        doNothing().when(category2).setVersion(Mockito.<Integer>any());
        doNothing().when(category2).setCategoryName(Mockito.<String>any());
        doNothing().when(category2).setProducts(Mockito.<List<Product>>any());
        category2.setCategoryName("Category Name");
        category2.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        category2.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        category2.setId(UUID.randomUUID());
        category2.setProducts(new ArrayList<>());
        category2.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        category2.setUpdatedBy("2020-03-01");
        category2.setVersion(1);
        Product model = mock(Product.class);
        when(model.getCategory()).thenReturn(category2);
        doNothing().when(model).setCreatedAt(Mockito.<OffsetDateTime>any());
        doNothing().when(model).setCreatedBy(Mockito.<String>any());
        doNothing().when(model).setId(Mockito.<UUID>any());
        doNothing().when(model).setUpdatedAt(Mockito.<OffsetDateTime>any());
        doNothing().when(model).setUpdatedBy(Mockito.<String>any());
        doNothing().when(model).setVersion(Mockito.<Integer>any());
        doNothing().when(model).setCategory(Mockito.<Category>any());
        doNothing().when(model).setDescription(Mockito.<String>any());
        doNothing().when(model).setImage(Mockito.<String>any());
        doNothing().when(model).setPrice(anyDouble());
        doNothing().when(model).setProductName(Mockito.<String>any());
        doNothing().when(model).setQuantity(anyInt());
        model.setCategory(category);
        model.setCreatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setCreatedBy("Jan 1, 2020 8:00am GMT+0100");
        model.setDescription("The characteristics of someone or something");
        model.setId(UUID.randomUUID());
        model.setImage("Image");
        model.setPrice(10.0d);
        model.setProductName("Product Name");
        model.setQuantity(1);
        model.setUpdatedAt(OffsetDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneOffset.UTC));
        model.setUpdatedBy("2020-03-01");
        model.setVersion(1);

        // Act
        productService.postprocessUpdateModel(model, new ProductReqDto());

        // Assert that nothing has changed
        verify(category2).getId();
        verify(category2).setCreatedAt(isA(OffsetDateTime.class));
        verify(model).setCreatedAt(isA(OffsetDateTime.class));
        verify(category2).setCreatedBy(eq("Jan 1, 2020 8:00am GMT+0100"));
        verify(model).setCreatedBy(eq("Jan 1, 2020 8:00am GMT+0100"));
        verify(category2).setId(isA(UUID.class));
        verify(model).setId(isA(UUID.class));
        verify(category2).setUpdatedAt(isA(OffsetDateTime.class));
        verify(model).setUpdatedAt(isA(OffsetDateTime.class));
        verify(category2).setUpdatedBy(eq("2020-03-01"));
        verify(model).setUpdatedBy(eq("2020-03-01"));
        verify(category2).setVersion(eq(1));
        verify(model).setVersion(eq(1));
        verify(category2).setCategoryName(eq("Category Name"));
        verify(category2).setProducts(isA(List.class));
        verify(model).getCategory();
        verify(model).setCategory(isA(Category.class));
        verify(model).setDescription(eq("The characteristics of someone or something"));
        verify(model).setImage(eq("Image"));
        verify(model).setPrice(eq(10.0d));
        verify(model).setProductName(eq("Product Name"));
        verify(model).setQuantity(eq(1));
    }
}
