package org.tdtu.ecommerceapi.service.rest;

import org.springframework.stereotype.Service;
import org.tdtu.ecommerceapi.dto.rest.request.ProductReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.ProductResDto;
import org.tdtu.ecommerceapi.exception.ConflictException;
import org.tdtu.ecommerceapi.model.rest.Category;
import org.tdtu.ecommerceapi.model.rest.Product;
import org.tdtu.ecommerceapi.repository.ProductRepository;
import org.tdtu.ecommerceapi.service.BaseService;
import org.tdtu.ecommerceapi.utils.MappingUtils;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService extends BaseService<Product, ProductReqDto, ProductResDto, ProductRepository> {
    private final CategoryService categoryService;

    public ProductService(ProductRepository repository, MappingUtils mappingUtils, CategoryService categoryService) {
        super(repository, mappingUtils);
        this.categoryService = categoryService;
    }

    @Override
    public ProductResDto create(ProductReqDto productReqDto) {
        Category category = categoryService.find(productReqDto.getCategoryId(), false);

        List<Product> products = category.getProducts();

        for (Product product : products) {
            if (productReqDto.getProductName().equals(product.getProductName())) {
                throw new ConflictException(Product.class, "name", productReqDto.getProductName());
            }
        }

        Product product = mappingUtils.mapFromDTO(productReqDto, Product.class);

        product.setCategory(category);
        product.setImage("defaultProduct.png");

        return mappingUtils.mapToDTO(repository.save(product), ProductResDto.class);
    }

    @Override
    protected void postprocessUpdateModel(Product model, ProductReqDto requestDTO) {
        if (requestDTO.getCategoryId() != model.getCategory().getId()) {
            Category category = categoryService.find(requestDTO.getCategoryId(), false);
            model.setCategory(category);
        }
    }
}