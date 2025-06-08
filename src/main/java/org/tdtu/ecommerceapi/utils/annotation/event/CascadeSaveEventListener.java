package org.tdtu.ecommerceapi.utils.annotation.event;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.util.ReflectionUtils;
import org.tdtu.ecommerceapi.model.GoogleAccount;
import org.tdtu.ecommerceapi.utils.annotation.CascadeSave;

import java.lang.reflect.Field;

public class CascadeSaveEventListener extends AbstractMongoEventListener<Object> {
    private final MongoOperations mongoOperations;

    public CascadeSaveEventListener(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Object> event) {
        Object source = event.getSource();
        ReflectionUtils.doWithFields(source.getClass(), field -> {
            if (field.isAnnotationPresent(CascadeSave.class)) {
                try {
                    field.setAccessible(true);
                    Object fieldValue = field.get(source);
                    if (fieldValue != null) {
                        Field idField = ReflectionUtils.findField(fieldValue.getClass(), "id");
                        idField.setAccessible(true);
                        Object idValue = idField.get(fieldValue);
                        if (idValue == null) {
                            // CASE ONLY: For GoogleAccount
                            if (fieldValue instanceof GoogleAccount) {
                                Field subField = ReflectionUtils.findField(fieldValue.getClass(), "sub");
                                subField.setAccessible(true);
                                Object subValue = subField.get(fieldValue);
                                if (subValue != null) {
                                    mongoOperations.save(fieldValue);
                                }
                            } else {
                                mongoOperations.save(fieldValue);
                            }
                        }
                    }
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(
                            "Cannot access field '" + field.getName() + "': " + e.getMessage(), e
                    );
                } finally {
                    field.setAccessible(false);
                }
            }
        });
    }
}
