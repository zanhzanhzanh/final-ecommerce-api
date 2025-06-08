package org.tdtu.ecommerceapi.utils.annotation.event;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.util.ReflectionUtils;
import org.tdtu.ecommerceapi.utils.annotation.CascadeDelete;

import java.util.Collection;

public class CascadeDeleteEventListener extends AbstractMongoEventListener<Object> {
    private final MongoOperations mongoOperations;

    public CascadeDeleteEventListener(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Object> event) {
        Document doc = event.getSource();
        Object id = doc.get("_id");
        Class<?> entityClass = event.getType();
        Object entity = mongoOperations.findById(id, entityClass);
        if (entity != null) {
            ReflectionUtils.doWithFields(entityClass, field -> {
                if (field.isAnnotationPresent(CascadeDelete.class)) {
                    field.setAccessible(true);
                    try {
                        Object referenced = field.get(entity);
                        if (referenced != null) {
                            if (referenced instanceof Collection) {
                                ((Collection<?>) referenced).forEach(mongoOperations::remove);
                            } else {
                                mongoOperations.remove(referenced);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Cannot access field '" + field.getName() + "': " + e.getMessage(), e);
                    }
                }
            });
        }
    }
}
