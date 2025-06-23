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
import org.tdtu.ecommerceapi.dto.rest.request.AddressReqDto;
import org.tdtu.ecommerceapi.dto.rest.request.OrderReqDto;
import org.tdtu.ecommerceapi.dto.rest.request.PlaceOrderReqDto;
import org.tdtu.ecommerceapi.enums.DeliveryStatus;
import org.tdtu.ecommerceapi.enums.PaymentStatus;
import org.tdtu.ecommerceapi.service.rest.OrderService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody @Valid PlaceOrderReqDto placeOrderReqDto) {
        return ResponseEntity.ok(orderService.placeOrder(placeOrderReqDto));
    }

    @PostMapping("/cal-temp-total-price")
    public ResponseEntity<?> calTempTotalPrice(@RequestBody @Valid PlaceOrderReqDto placeOrderReqDto) {
        return ResponseEntity.ok(Map.of("tempTotalPrice", orderService.calTempTotalPrice(placeOrderReqDto)));
    }

    @PostMapping("/take-available-coupon")
    public ResponseEntity<?> takeAvailableCoupon(JwtAuthenticationToken jwtToken, @RequestBody List<UUID> promotionIds) {
        return ResponseEntity.ok(orderService.getAvailablePromotions(
                UUID.fromString(jwtToken.getToken().getClaimAsString("sub")),promotionIds));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getById(id, false));
    }

    @GetMapping("/get-by-account/{accountId}")
    public ResponseEntity<?> getByAccountId(@PathVariable UUID accountId) {
        return ResponseEntity.ok(orderService.findByAccount(accountId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable UUID id,
            @RequestParam PaymentStatus paymentStatus,
            @RequestParam DeliveryStatus deliveryStatus) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, paymentStatus, deliveryStatus));
    }

    // TODO: Delete this
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteById(@PathVariable UUID id) {
//        orderService.deleteById(id);
//        return ResponseEntity.ok(null);
//    }

    @GetMapping
    public ResponseEntity<?> getList(
            @PageableDefault(
                    sort = {"createdAt"},
                    direction = Sort.Direction.DESC)
            @ParameterObject Pageable pageable,
            @RequestParam(required = false, defaultValue = "") String[] filter) {
        ApiPageableResponse orderPage = orderService.getList(filter, pageable);
        return ResponseEntity.ok(orderPage);
    }
}
