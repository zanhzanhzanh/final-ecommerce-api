package org.tdtu.ecommerceapi.service.rest;

import org.springframework.stereotype.Service;
import org.tdtu.ecommerceapi.dto.rest.request.CategoryReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.CategoryResDto;
import org.tdtu.ecommerceapi.model.rest.Category;
import org.tdtu.ecommerceapi.repository.CategoryRepository;
import org.tdtu.ecommerceapi.service.BaseService;
import org.tdtu.ecommerceapi.utils.MappingUtils;

@Service
public class CategoryService extends BaseService<Category, CategoryReqDto, CategoryResDto, CategoryRepository> {
    public CategoryService(CategoryRepository repository, MappingUtils mappingUtils) {
        super(repository, mappingUtils);
    }
}
