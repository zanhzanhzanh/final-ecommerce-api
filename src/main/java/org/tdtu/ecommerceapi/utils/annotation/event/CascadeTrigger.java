package org.tdtu.ecommerceapi.utils.annotation.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;

@Configuration
public class CascadeTrigger {
    @Bean
    public CascadeSaveEventListener cascadeSaveEventListener(MongoOperations mongoOperations) {
        return new CascadeSaveEventListener(mongoOperations);
    }

    @Bean
    public CascadeDeleteEventListener cascadeDeleteEventListener(MongoOperations mongoOperations) {
        return new CascadeDeleteEventListener(mongoOperations);
    }
}