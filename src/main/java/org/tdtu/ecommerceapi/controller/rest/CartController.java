package org.tdtu.ecommerceapi.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.tdtu.ecommerceapi.dto.api.ApiPageableResponse;
import org.tdtu.ecommerceapi.dto.rest.request.CartQuantityReqDto;
import org.tdtu.ecommerceapi.dto.rest.request.CartReqDto;
import org.tdtu.ecommerceapi.service.rest.CartService;

import java.util.UUID;

@RestController
@RequestMapping("/v1/carts")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping("/update-item-quantity")
    public ResponseEntity<?> updateCartItemQuantity(@RequestBody @Valid CartQuantityReqDto requestDTO) {
        return ResponseEntity.ok(cartService.updateCartItemQuantity(requestDTO));
    }

    @PostMapping("/refresh-cart/{cartId}")
    public ResponseEntity<?> refreshCart(@PathVariable UUID cartId) {
        return ResponseEntity.ok(cartService.refreshCart(cartId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(cartService.getById(id, false));
    }

    @GetMapping("/get-by-token")
    public ResponseEntity<?> getByToken(JwtAuthenticationToken jwtToken) {
        return ResponseEntity.ok(cartService.getByToken(
                UUID.fromString(jwtToken.getToken().getClaimAsString("sub"))));
    }

    @PostMapping
    public ResponseEntity<?> create(JwtAuthenticationToken jwtToken) {
        CartReqDto requestDTO = new CartReqDto();
        requestDTO.setAccountId(jwtToken.getToken().getClaimAsString("sub"));
        return ResponseEntity.ok(cartService.create(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateById(JwtAuthenticationToken jwtToken,
                                        @PathVariable UUID id) {
        CartReqDto requestDTO = new CartReqDto();
        requestDTO.setAccountId(jwtToken.getToken().getClaimAsString("sub"));
        return ResponseEntity.ok(cartService.updateById(id, requestDTO));
    }

    // TODO: Delete this
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
//        cartService.deleteById(id);
//        return ResponseEntity.ok(null);
//    }

    @GetMapping
    public ResponseEntity<?> getList(
            @PageableDefault(
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC)
            @ParameterObject Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String[] filter) {
        ApiPageableResponse cartPage = cartService.getList(filter, pageable);
        return ResponseEntity.ok(cartPage);
    }
}
