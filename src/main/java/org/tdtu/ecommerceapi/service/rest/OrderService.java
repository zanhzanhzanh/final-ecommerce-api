package org.tdtu.ecommerceapi.service.rest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tdtu.ecommerceapi.dto.rest.request.OrderReqDto;
import org.tdtu.ecommerceapi.dto.rest.request.PlaceOrderReqDto;
import org.tdtu.ecommerceapi.dto.rest.response.OrderResDto;
import org.tdtu.ecommerceapi.enums.DeliveryStatus;
import org.tdtu.ecommerceapi.enums.PaymentStatus;
import org.tdtu.ecommerceapi.enums.PromotionType;
import org.tdtu.ecommerceapi.enums.ProportionType;
import org.tdtu.ecommerceapi.exception.BadRequestException;
import org.tdtu.ecommerceapi.exception.NotFoundException;
import org.tdtu.ecommerceapi.model.Account;
import org.tdtu.ecommerceapi.model.rest.*;
import org.tdtu.ecommerceapi.repository.*;
import org.tdtu.ecommerceapi.service.BaseService;
import org.tdtu.ecommerceapi.utils.MappingUtils;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderService extends BaseService<Order, OrderReqDto, OrderResDto, OrderRepository> {
    private final CartRepository cartRepository;
    private final AddressService addressService;
    private final PromotionService promotionService;
    private final AccountRepository accountRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderRepository repository,
                        MappingUtils mappingUtils,
                        CartRepository cardRepository,
                        AddressService addressService,
                        PromotionService promotionService,
                        AccountRepository accountRepository,
                        CartItemRepository cartItemRepository,
                        OrderItemRepository orderItemRepository) {
        super(repository, mappingUtils);
        this.cartRepository = cardRepository;
        this.addressService = addressService;
        this.promotionService = promotionService;
        this.accountRepository = accountRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderResDto> findByAccount(UUID accountId) {
        return mappingUtils.mapListToDTO(
                repository.findByAccount(accountId), OrderResDto.class);
    }

    public List<UUID> getAvailablePromotions(UUID accountId, List<UUID> promotionIds) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException(Account.class, "id", accountId.toString()));
        if (promotionIds == null || promotionIds.isEmpty()) {
            return Collections.emptyList();
        }
        return promotionIds.stream()
                .filter(promotionId -> !account.getPromotionIds().contains(promotionId))
                .collect(Collectors.toList());
    }

    public double calTempTotalPrice(PlaceOrderReqDto placeOrderReqDto) {
        Cart cart = cartRepository.findById(placeOrderReqDto.getCartId()).orElseThrow(
                () -> new NotFoundException(Cart.class, "id", placeOrderReqDto.getCartId().toString()));

        Account account = accountRepository.findById(cart.getAccount().getId()).orElse(null);
        if (account == null) {
            throw new BadRequestException("Cart must have an account to calculate total price");
        }

        List<Promotion> promotions = placeOrderReqDto.getPromotionIds().stream()
                .map(promotionId -> promotionService.find(promotionId, false))
                .filter(promotion -> promotion != null &&
                        !account.getPromotionIds().contains(promotion.getId()) &&
                        OffsetDateTime.now().isAfter(promotion.getStartDate()) &&
                        OffsetDateTime.now().isBefore(promotion.getEndDate()))
                .collect(Collectors.toList());

        Promotion promotionAllProducts = findPromotionByType(promotions, PromotionType.ALL_PRODUCTS);
        Promotion promotionOrderTotal = findPromotionByType(promotions, PromotionType.ORDER_TOTAL);
        Promotion promotionSpecificProducts = findPromotionByType(promotions, PromotionType.SPECIFIC_PRODUCTS);

        // Calculate total price from cart items with promotions
        double totalPrice = cart.getCartItems().stream()
                .mapToDouble(cartItem -> {
                    double finalValueOfItem = cartItem.getProductPrice();
                    double delta = calculateDiscount(finalValueOfItem, cartItem, promotionAllProducts, promotionSpecificProducts);
                    return (finalValueOfItem - delta) * cartItem.getQuantity();
                })
                .sum();

        // Apply order total promotion if eligible
        if (promotionOrderTotal != null && promotionOrderTotal.getMinOrderValue() <= totalPrice) {
            totalPrice = calculateOrderTotalDiscount(totalPrice, promotionOrderTotal);
        }

        return totalPrice;
    }

    @Transactional
    public OrderResDto placeOrder(PlaceOrderReqDto placeOrderReqDto) {
        Cart cart = cartRepository.findById(placeOrderReqDto.getCartId()).orElseThrow(
                () -> new NotFoundException(Cart.class, "id", placeOrderReqDto.getCartId().toString()));

        Account account = accountRepository.findById(cart.getAccount().getId()).orElse(null);
        if (account == null) {
            throw new BadRequestException("Cart must have an account to place an order");
        } else {
            if (account.getEmail() == null)
                throw new BadRequestException("Account must have an email to place an order");
            // TODO: Check this if needed
//            if(account.getPhoneNumber() == null) throw new BadRequestException("Account must have a phone number to place an order");
            if (account.getAddresses().stream().noneMatch(address -> address.getId().equals(placeOrderReqDto.getAddressId()))) {
                throw new BadRequestException("Account must have a valid address to place an order");
            }
        }

        Order order = new Order();
        order.setPaymentStatus(PaymentStatus.PENDING);
        order.setDeliveryStatus(DeliveryStatus.PENDING);
        order.setShipCOD(placeOrderReqDto.isShipCOD());
        order.setOrderDate(OffsetDateTime.now());
        order.setAccount(cart.getAccount());
        order.setAddress(addressService.find(placeOrderReqDto.getAddressId(), false));

        List<Promotion> promotions = placeOrderReqDto.getPromotionIds().stream()
                .map(promotionId -> promotionService.find(promotionId, false))
                // Remove promotions that the account already has or that are not valid anymore
                .filter(promotion -> promotion != null &&
                        !account.getPromotionIds().contains(promotion.getId()) &&
                        OffsetDateTime.now().isAfter(promotion.getStartDate()) &&
                        OffsetDateTime.now().isBefore(promotion.getEndDate()))
                .collect(Collectors.toList());

        Promotion promotionAllProducts = findPromotionByType(promotions, PromotionType.ALL_PRODUCTS);
        Promotion promotionOrderTotal = findPromotionByType(promotions, PromotionType.ORDER_TOTAL);
        Promotion promotionSpecificProducts = findPromotionByType(promotions, PromotionType.SPECIFIC_PRODUCTS);

        // Map Cart Items to Order Items with Promotions
        List<OrderItem> orderItems = cart.getCartItems().stream()
                .map(cartItem -> createOrderItem(
                        cartItem,
                        promotionAllProducts,
                        promotionSpecificProducts,
                        order
                ))
                .collect(Collectors.toList());

        orderItems = orderItemRepository.saveAll(orderItems);
        order.setOrderItems(orderItems);

        // Calculate total price from discounted order items
        double totalPrice = orderItems.stream()
                .mapToDouble(OrderItem::getTotalPriceProduct)
                .sum();
        // Apply order total promotion if eligible
        if (promotionOrderTotal != null && promotionOrderTotal.getMinOrderValue() <= totalPrice) {
            totalPrice = calculateOrderTotalDiscount(totalPrice, promotionOrderTotal);
        }

        order.setTotalPrice(totalPrice);
        order.setUsedPromotions(Stream.of(
                promotionAllProducts,
                promotionOrderTotal,
                promotionSpecificProducts
        ).filter(Objects::nonNull).collect(Collectors.toSet()));

        // Delete cart items
        // TODO: Check this later
        cartItemRepository.deleteAll(cart.getCartItems());
        cart.setCartItems(new ArrayList<>());
        cartRepository.save(cart);

        // Set the account's promotion IDs to include the used promotions
        // TODO: Check this later
        account.getPromotionIds().addAll(Stream.of(
                                promotionAllProducts,
                                promotionOrderTotal,
                                promotionSpecificProducts
                        ).filter(Objects::nonNull)
                        .map(Promotion::getId)
                        .collect(Collectors.toSet())
        );

        // WARN: Active Cascade Save
        accountRepository.save(account);

        // Save the order and return the response DTO
        return mappingUtils.mapToDTO(repository.save(order), OrderResDto.class);
    }

    private Promotion findPromotionByType(List<Promotion> promotions, PromotionType type) {
        return promotions.stream()
                .filter(p -> p.getPromotionType() == type)
                .findFirst()
                .orElse(null);
    }

    private OrderItem createOrderItem(CartItem cartItem, Promotion promotionAllProducts, Promotion promotionSpecificProducts, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setQuantity(cartItem.getQuantity());

        double finalValueOfItem = cartItem.getProductPrice();
        double delta = calculateDiscount(finalValueOfItem, cartItem, promotionAllProducts, promotionSpecificProducts);

        double finalPrice = (finalValueOfItem - delta) * cartItem.getQuantity();
        orderItem.setTotalPriceProduct(finalPrice < 0 ? 0 : finalPrice);
        orderItem.setUpdatePriceProduct(finalValueOfItem - delta < 0 ? 0 : finalValueOfItem - delta);
        orderItem.setOrder(order);
        return orderItem;
    }

    private double calculateDiscount(double finalValueOfItem, CartItem cartItem, Promotion promotionAllProducts, Promotion promotionSpecificProducts) {
        double delta = 0.0;

        if (promotionAllProducts != null && promotionAllProducts.getProportionType() == ProportionType.PERCENTAGE) {
            delta = finalValueOfItem * promotionAllProducts.getDiscountAmount() / 100;
        }

        if (promotionSpecificProducts != null) {
            boolean isProductEligible = promotionSpecificProducts.getProductIds()
                    .stream()
                    .anyMatch(productId -> productId.equals(cartItem.getProduct().getId()));

            if (isProductEligible) {
                delta = calculateSpecificProductDiscount(finalValueOfItem, promotionSpecificProducts);
            }
        }

        return delta;
    }

    private double calculateSpecificProductDiscount(double finalValueOfItem, Promotion promotionSpecificProducts) {
        double delta = 0.0;

        if (promotionSpecificProducts.getProportionType() == ProportionType.PERCENTAGE) {
            delta = finalValueOfItem * promotionSpecificProducts.getDiscountAmount() / 100;
        } else if (promotionSpecificProducts.getProportionType() == ProportionType.ABSOLUTE) {
            delta = Math.min(promotionSpecificProducts.getDiscountAmount(), finalValueOfItem);
        }

        return delta;
    }

    private double calculateOrderTotalDiscount(double totalPrice, Promotion promotionOrderTotal) {
        if (promotionOrderTotal.getProportionType() == ProportionType.PERCENTAGE) {
            return Math.max(totalPrice - (totalPrice * promotionOrderTotal.getDiscountAmount() / 100), 0);
        }
        if (promotionOrderTotal.getProportionType() == ProportionType.ABSOLUTE) {
            return Math.max(totalPrice - promotionOrderTotal.getDiscountAmount(), 0);
        }
        return totalPrice;
    }

    public OrderResDto updateOrderStatus(UUID orderId, PaymentStatus paymentStatus, DeliveryStatus deliveryStatus) {
        Order order = find(orderId, false);
        order.setPaymentStatus(paymentStatus);
        order.setDeliveryStatus(deliveryStatus);
        return mappingUtils.mapToDTO(repository.save(order), OrderResDto.class);
    }
}