package org.tdtu.ecommerceapi.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

@Data
public class Token {
    private final Jwt token;
    private final Collection<GrantedAuthority> authorities;
}
