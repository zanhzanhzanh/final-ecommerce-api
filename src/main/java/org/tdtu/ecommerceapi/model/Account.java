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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.tdtu.ecommerceapi.model.rest.Address;
import org.tdtu.ecommerceapi.utils.annotation.CascadeSave;

import java.util.*;

@Document(collection = "accounts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
//@JsonIgnoreProperties(ignoreUnknown = true)
// @Audited
// @SQLDelete(sql = "UPDATE account SET is_deleted = TRUE WHERE id = ?")
// @Where(clause = "is_deleted = false")
public class Account extends BaseModel implements UserDetails {

    @NotNull
    private String username;

    @NotNull
    private String email;

    private String password;

    private Integer birthYear;

    private String phoneNumber;

    private Set<UUID> promotionIds = new HashSet<>();

    @DocumentReference(lazy = true, lookup = "{ 'account' : ?#{#self._id} }")
    @ReadOnlyProperty
    private List<Address> addresses;

    @DocumentReference(lazy = true)
    private AppGroup group;

    @DocumentReference(lazy = true)
    @CascadeSave
    private GoogleAccount googleAccount;

//    @Field(name = "meta_account_id")
//    private MetaAccount metaAccount;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        if (this.group != null && this.group.getName() != null) {
            authorities.add(new SimpleGrantedAuthority(this.group.getName()));
        }

//         authorities.add(new SimpleGrantedAuthority("SCOPE_read"));
//         authorities.add(new SimpleGrantedAuthority("SCOPE_write"));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
