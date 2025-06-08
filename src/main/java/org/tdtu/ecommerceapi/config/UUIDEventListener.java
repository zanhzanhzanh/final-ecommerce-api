package org.tdtu.ecommerceapi.config;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import org.tdtu.ecommerceapi.model.BaseModel;

import java.util.UUID;

@Component
public class UUIDEventListener extends AbstractMongoEventListener<BaseModel> {
    /**
     * This method is called before the entity is converted to a MongoDB document.
     * It sets a new UUID as the ID if the entity does not already have one.
     * @apiNote  This method is discouraged in the case your TaskExecutor is asynchronous
     * as the ID may be generated after the object has been converted.
     * @param event The event containing the entity to be converted.
     */
    @Override
    public void onBeforeConvert(BeforeConvertEvent<BaseModel> event) {
        BaseModel entity = event.getSource();
        if (entity.getId() == null) {
            entity.setId(UUID.randomUUID());
        }
    }
}
