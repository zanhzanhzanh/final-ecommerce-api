package org.tdtu.ecommerceapi.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "google_accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
// @Audited
// @SQLDelete(sql = "UPDATE account SET is_deleted = TRUE WHERE id = ?")
// @Where(clause = "is_deleted = false")
public class GoogleAccount extends BaseModel {

    @Field(name = "google_account_id")
    @Indexed(unique = true)
    @NotNull
    private String sub;

    @NotNull
    private String name;

    @NotNull
    private String email;

    @NotNull
    private String email_verified;

    @NotNull
    private String family_name;

    @NotNull
    private String given_name;

    @Field(name = "picture_url")
    @NotNull
    private String picture;

    @DocumentReference(lazy = true)
    private Account account;
}
