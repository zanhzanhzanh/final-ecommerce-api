package org.tdtu.ecommerceapi.controller.rest;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.tdtu.ecommerceapi.dto.rest.request.CategoryReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.CategoryResDto;
import org.tdtu.ecommerceapi.model.rest.Category;
import org.tdtu.ecommerceapi.service.rest.CategoryService;

@ContextConfiguration(classes = {CategoryController.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class CategoryControllerTest extends BaseTest<
        CategoryController,
        CategoryService,
        Category,
        CategoryReqDto,
        CategoryResDto> {
    @Autowired
    private CategoryController categoryController;

    @MockitoBean
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        String endpoint = "/v1/categories";
        setupController(
                categoryController,
                categoryService,
                endpoint);
    }

    /**
     * Method under test: {@link CategoryController#getById(UUID)}
     */
    @Test
    void testGetByIdMethod() throws Exception {
        testGetById();
    }

    /**
     * Method under test: {@link CategoryController#create(CategoryReqDto)}
     */
    @Test
    void testCreateMethod() throws Exception {
        testCreate();
    }

    /**
     * Method under test:
     * {@link CategoryController#updateById(UUID, CategoryReqDto)}
     */
    @Test
    void testUpdateByIdMethod() throws Exception {
        testUpdateById();
    }

    /**
     * Method under test: {@link CategoryController#deleteById(UUID)}
     */
    @Test
    void testDeleteByIdMethod() throws Exception {
        testDeleteById();
    }

    /**
     * Method under test: {@link CategoryController#deleteById(UUID)}
     */
    @Test
    void testDeleteByIdWithInvalidContentTypeMethod() throws Exception {
        testDeleteByIdWithInvalidContentType();
    }

    /**
     * Method under test: {@link CategoryController#getList(Pageable, String[])}
     */
    @Test
    void testGetListMethod() throws Exception {
        testGetList();
    }
}
