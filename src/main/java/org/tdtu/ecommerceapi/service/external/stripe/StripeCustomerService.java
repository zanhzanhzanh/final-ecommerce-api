package org.tdtu.ecommerceapi.service.external.stripe;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StripeCustomerService extends StripeService {

    @SneakyThrows(StripeException.class)
    public Customer create(Map<String, Object> params) {
        return Customer.create(params, requestOptions);
    }
}
