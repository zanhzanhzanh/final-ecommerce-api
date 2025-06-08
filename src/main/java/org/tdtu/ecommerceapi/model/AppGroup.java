package org.tdtu.ecommerceapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

@Document(collection = "app_group")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
//@DynamicUpdate
// @Audited
// @SQLDelete(sql = "UPDATE app_group SET is_deleted = TRUE WHERE id = ?")
// @Where(clause = "is_deleted = false")
public class AppGroup extends BaseModel {

    @NotNull
    private String name;

    @DocumentReference(lazy = true, lookup = "{ 'account' : ?#{#self._id} }")
    @ReadOnlyProperty
    private List<Account> accounts;

//    @PreRemove
//    private void preRemove() {
//        accounts.forEach(account -> account.setGroupId(null));
//    }
}
