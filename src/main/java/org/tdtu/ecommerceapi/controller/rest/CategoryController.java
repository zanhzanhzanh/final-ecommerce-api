package org.tdtu.ecommerceapi.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tdtu.ecommerceapi.dto.api.ApiPageableResponse;
import org.tdtu.ecommerceapi.dto.rest.request.CategoryReqDto;
import org.tdtu.ecommerceapi.service.rest.CategoryService;

import java.util.UUID;

@RestController
@RequestMapping("/v1/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

//    @GetMapping
//    public ResponseEntity<?> getAllCategories() {
//        return ResponseEntity.ok(categoryService.getAll());
//    }

//    @GetMapping("/{id}")
//    public ResponseEntity<?> getById(UUID id) {
//        long start = System.currentTimeMillis();
//        Category category = categoryRepository.findById(id).orElseThrow();
//        if (false) {
//            category.getProducts().forEach(product -> product.setCategory(null));
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("Time taken!!!!!!!!: " + (end - start) + "ms");
//        return ResponseEntity.ok(category);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.getById(id, false));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid CategoryReqDto requestDTO) {
        return ResponseEntity.ok(categoryService.create(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable UUID id, @RequestBody @Valid CategoryReqDto requestDTO) {
        return ResponseEntity.ok(categoryService.updateById(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
        categoryService.deleteById(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<?> getList(
            @PageableDefault(
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC)
            @ParameterObject Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String[] filter) {
        ApiPageableResponse categoryPage = categoryService.getList(filter, pageable);
        return ResponseEntity.ok(categoryPage);
    }
}
