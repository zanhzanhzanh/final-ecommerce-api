package org.tdtu.ecommerceapi.model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass // Ensure that won't have a separate representation as table
//@EntityListeners(AuditingEntityListener.class) // Not needed for MongoDB
public abstract class BaseModel implements Serializable {
    @Id
    private UUID id;

    @CreatedDate
    private OffsetDateTime createdAt;

    @LastModifiedDate
    private OffsetDateTime updatedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

     @Version
     private Integer version;

    // @Field(name = "is_deleted")
    // private boolean isDeleted;

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof BaseModel)) return false;
        BaseModel that = (BaseModel) object;
        return this.id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
