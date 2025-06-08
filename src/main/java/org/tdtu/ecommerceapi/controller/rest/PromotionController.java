package org.tdtu.ecommerceapi.controller.rest;

import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.tdtu.ecommerceapi.dto.api.ApiPageableResponse;
import org.tdtu.ecommerceapi.dto.group.OnCreate;
import org.tdtu.ecommerceapi.dto.rest.request.PromotionReqDto;
import org.tdtu.ecommerceapi.service.rest.PromotionService;

import java.util.UUID;

@RestController
@RequestMapping("/v1/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(promotionService.getById(id, false));
    }

    @GetMapping("/get-by-code/{code}")
    public ResponseEntity<?> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(promotionService.getByCode(code));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Validated({OnCreate.class, Default.class}) PromotionReqDto requestDTO) {
        return ResponseEntity.ok(promotionService.create(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable UUID id, @RequestBody PromotionReqDto requestDTO) {
        return ResponseEntity.ok(promotionService.updateById(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
        promotionService.deleteById(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<?> getList(
            @PageableDefault(
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC)
            @ParameterObject Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String[] filter) {
        ApiPageableResponse productPage = promotionService.getList(filter, pageable);
        return ResponseEntity.ok(productPage);
    }
}
