package org.tdtu.ecommerceapi.controller.external.stripe;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tdtu.ecommerceapi.service.external.stripe.StripeCustomerService;

@Tag(name = "Stripe Customers")
@RestController
@RequestMapping("/stripe/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final StripeCustomerService stripeCustomerService;

//    @PostMapping()
//    public ResponseEntity<String> create(@RequestBody Map<String, Object> params) {
//        return ResponseEntity.ok(stripeCustomerService.create(params).toJson());
//    }
}
