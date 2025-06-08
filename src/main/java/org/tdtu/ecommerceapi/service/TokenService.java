package org.tdtu.ecommerceapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.tdtu.ecommerceapi.model.Account;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {
    @Value("${jwt.validity.hours}")
    private String jwtValidityHours;

    private final JwtEncoder jwtEncoder;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        Account account = (Account) authentication.getPrincipal();
        String authorities =
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(" "));
        JwtClaimsSet claims =
                JwtClaimsSet.builder()
                        .issuer("http://foobar.com")
                        .issuedAt(now)
                        .expiresAt(now.plus(Long.parseLong(jwtValidityHours), ChronoUnit.HOURS))

                        .subject(account.getId().toString())
                        .claim("username", account.getUsername() != null ? account.getUsername() : "")
                        .claim("email", account.getEmail() != null ? account.getEmail() : "")
                        .claim("birthYear", account.getBirthYear() != null ? account.getBirthYear() : 0)
                        .claim("phoneNumber", account.getPhoneNumber() != null ? account.getPhoneNumber() : "")
                        .claim("authorities", authorities)

                        .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
