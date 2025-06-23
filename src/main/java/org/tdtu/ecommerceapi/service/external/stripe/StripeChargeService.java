package org.tdtu.ecommerceapi.service.external.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Coupon;
import com.stripe.model.checkout.Session;
import com.stripe.param.CouponCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.tdtu.ecommerceapi.enums.PaymentStatus;
import org.tdtu.ecommerceapi.enums.PromotionType;
import org.tdtu.ecommerceapi.enums.ProportionType;
import org.tdtu.ecommerceapi.model.rest.*;
import org.tdtu.ecommerceapi.repository.OrderRepository;
import org.tdtu.ecommerceapi.repository.ProductRepository;
import org.tdtu.ecommerceapi.service.rest.OrderService;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StripeChargeService extends StripeService {
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public StripeChargeService(OrderService orderService,
                               OrderRepository orderRepository,
                               ProductRepository productRepository) {
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @SneakyThrows(StripeException.class)
    public String createCheckoutSession(UUID orderId) {
        Order order = orderService.find(orderId, false);

        List<SessionCreateParams.Discount> discounts = new ArrayList<>();
        for (Promotion promotion : order.getUsedPromotions()) {
            if (promotion.getPromotionType() == PromotionType.ORDER_TOTAL) {
                CouponCreateParams.Builder couponBuilder = CouponCreateParams.builder()
                        .setName(promotion.getPromotionName())
                        .setCurrency("vnd")
                        .setDuration(CouponCreateParams.Duration.ONCE);

                if (promotion.getProportionType() == ProportionType.PERCENTAGE) {
                    couponBuilder.setPercentOff(BigDecimal.valueOf(promotion.getDiscountAmount()));
                } else if (promotion.getProportionType() == ProportionType.ABSOLUTE) {
                    couponBuilder.setAmountOff((long) promotion.getDiscountAmount().doubleValue()); // Convert to cents
                }

                String couponId = Coupon.create(couponBuilder.build()).getId();

                discounts.add(SessionCreateParams.Discount.builder()
                        .setCoupon(couponId)
                        .build());
            }
        }

        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                // TODO: Build in application.properties for FE and + orderId
                // TODO: Or use for update Order Or in Webhook
                .setSuccessUrl(clientURI + "/stripe/success?orderId=" + orderId.toString())
                .setCancelUrl(clientURI + "/stripe/cancel?orderId=" + orderId.toString())
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .putMetadata("orderId", orderId.toString())
                // TODO: Check if this need
                .putMetadata("totalPrice", String.valueOf(order.getTotalPrice()))
                .setExpiresAt(Instant.now().getEpochSecond() + 60 * 60);
//                .setBillingAddressCollection(SessionCreateParams.BillingAddressCollection.REQUIRED);

        if (!discounts.isEmpty()) {
            paramsBuilder.addAllDiscount((discounts));
        }

        for (OrderItem orderItem : order.getOrderItems()) {
            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setCurrency("vnd")
                                            .setUnitAmount((long) orderItem.getUpdatePriceProduct())
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .setName(orderItem.getProduct().getProductName())
                                                            // TODO: Set Image
                                                            .build()
                                            )
                                            .build()
                            )
                            .setQuantity((long) orderItem.getQuantity())
                            .build()
            );
        }

        SessionCreateParams params = paramsBuilder.build();
        Session session = Session.create(params, requestOptions);

        return session.getUrl();
    }

    public void updateOrderAndProduct(UUID orderId) {
        Order order = orderService.find(orderId, false);

        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = productRepository.findById(orderItem.getProduct().getId()).orElse(null);
            if (product != null) {
                product.setQuantity(product.getQuantity() - orderItem.getQuantity());
                productRepository.save(product);
            }
        }

        order.setPaymentStatus(PaymentStatus.COMPLETED);
        orderRepository.save(order);
    }
}
