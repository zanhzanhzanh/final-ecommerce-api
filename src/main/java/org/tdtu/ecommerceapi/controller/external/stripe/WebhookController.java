package org.tdtu.ecommerceapi.controller.external.stripe;

import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.tdtu.ecommerceapi.exception.BadRequestException;
import org.tdtu.ecommerceapi.service.external.stripe.StripeChargeService;

import java.util.UUID;

@Tag(name = "Stripe Webhook")
@RestController
@RequestMapping("/stripe/webhook")
@RequiredArgsConstructor
public class WebhookController {

    @Value("${stripe.webhook.signing-secret}")
    private String webhookSecret;
    private final StripeChargeService stripeChargeService;

    @PostMapping
    public void webhook(
            @RequestHeader(value = "Stripe-Signature", required = true) String sigHeader,
            @RequestBody String payload) {
        Event event = null;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException | JsonSyntaxException e) {
            throw new BadRequestException(e.getMessage());
        }

        StripeObject stripeObject = getStripeObject(event);
        switch (event.getType()) {
            case "customer.created":
//                Customer customer = (Customer) stripeObject;
                break;
            case "checkout.session.completed":
                Session session = (Session) stripeObject;
                String orderId = session.getMetadata().get("orderId");
                if (orderId != null) {
                    stripeChargeService.updateOrderAndProduct(UUID.fromString(orderId));
                } else {
                    throw new BadRequestException("Order ID not found in metadata");
                }
                break;
                // TODO: Handle if fail
            default:
                throw new BadRequestException("Unexpected event type");
        }
    }

    private static StripeObject getStripeObject(Event event) {
        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = null;
        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            // Deserialization failed, probably due to an API version mismatch.
            throw new IllegalStateException(
                    String.format("Unable to deserialize event data object for %s", event));
        }
        return stripeObject;
    }
}
