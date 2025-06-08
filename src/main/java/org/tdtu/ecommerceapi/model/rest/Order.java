package org.tdtu.ecommerceapi.model.rest;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.tdtu.ecommerceapi.enums.DeliveryStatus;
import org.tdtu.ecommerceapi.enums.PaymentStatus;
import org.tdtu.ecommerceapi.model.Account;
import org.tdtu.ecommerceapi.model.BaseModel;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseModel {
    @PositiveOrZero
    private double totalPrice;

    @Enumerated(EnumType.STRING)
    @NotNull
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @NotNull
    private DeliveryStatus deliveryStatus;

    @NotNull
    private boolean isShipCOD;

    @NotNull
    private OffsetDateTime orderDate;

    @DocumentReference(lazy = true)
    private Account account;

    @DocumentReference(lazy = true)
    private Address address;

    @DocumentReference(lazy = true)
//    @DBRef(lazy = true)
    private Set<Promotion> usedPromotions = new HashSet<>();

    @DocumentReference(lazy = true)
    private List<OrderItem> orderItems = new ArrayList<>();
}
