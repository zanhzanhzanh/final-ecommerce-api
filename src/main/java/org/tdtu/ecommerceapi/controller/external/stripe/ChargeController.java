package org.tdtu.ecommerceapi.controller.external.stripe;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tdtu.ecommerceapi.service.external.stripe.StripeChargeService;

import java.util.UUID;

@Tag(name = "Stripe Charges")
@RestController
@RequestMapping("/stripe/charges")
@RequiredArgsConstructor
public class ChargeController {

    private final StripeChargeService stripeChargeService;

    /**
     * @apiNote FE send credit details to Stripe and receive a token, BE will use that token to charge
     * credit card
     */
//    @PostMapping()
//    public ResponseEntity<String> create(@Valid @RequestBody StripeChargeRequestDTO requestDTO) {
//        return ResponseEntity.ok(stripeChargeService.create(requestDTO).toJson());
//    }

    @PostMapping()
    public ResponseEntity<String> create(@RequestParam UUID orderId) {
        return ResponseEntity.ok(stripeChargeService.createCheckoutSession(orderId));
    }
}
