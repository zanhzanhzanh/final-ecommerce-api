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
import org.tdtu.ecommerceapi.dto.rest.request.AddressReqDto;
import org.tdtu.ecommerceapi.service.rest.AddressService;

import java.util.UUID;

@RestController
@RequestMapping("/v1/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(addressService.getById(id, false));
    }

    @GetMapping("/get-by-account/{accountId}")
    public ResponseEntity<?> getByAccountId(@PathVariable UUID accountId) {
        return ResponseEntity.ok(addressService.findByAccount(accountId));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid AddressReqDto requestDTO) {
        return ResponseEntity.ok(addressService.create(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(@PathVariable UUID id, @RequestBody AddressReqDto requestDTO) {
        return ResponseEntity.ok(addressService.updateById(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
        addressService.deleteById(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping
    public ResponseEntity<?> getList(
            @PageableDefault(
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC)
            @ParameterObject Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String[] filter) {
        ApiPageableResponse addressPage = addressService.getList(filter, pageable);
        return ResponseEntity.ok(addressPage);
    }
}
