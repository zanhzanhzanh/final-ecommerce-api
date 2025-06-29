package org.tdtu.ecommerceapi.config.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.tdtu.ecommerceapi.repository.AccountRepository;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${server.security.disabled}")
    private String isSecurityDisabled;

    @Value("${public.key.location}")
    private RSAPublicKey publicKey;

    @Value("${private.key.location}")
    private RSAPrivateKey privateKey;

    private final AccountRepository accountRepository;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity httpSecurity, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity
                .getSharedObject(AuthenticationManagerBuilder.class)
                .parentAuthenticationManager(null); // Avoid
        // infinite
        // loop:
        // https://stackoverflow.com/questions/27956378/infinte-loop-when-bad-credentials-are-entered-in-spring-security-form-login

        authenticationManagerBuilder
                .userDetailsService(email -> accountRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new BadCredentialsException(null)))
                .passwordEncoder(bCryptPasswordEncoder);

        return authenticationManagerBuilder.build();
    }

//    @Bean
//    public AuthenticationManager authenticationManager(
//            AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

    @Bean
    public JwtEncoder jwtEncoder() {
        JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    private final String[] byPassPath =
            new String[]{
                    "**/api-docs/**", "/swagger-resources/**", "/swagger-ui/**", "**/token", "**/signup", "**/stripe/webhook", "**/data/**",
//                    "v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**", "/token", "/signup"
            };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        if (Boolean.parseBoolean(isSecurityDisabled)) {
            httpSecurity.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        } else {
            httpSecurity
                    .authorizeHttpRequests(authorize -> authorize
                                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                    .requestMatchers(HttpMethod.POST, "/v1/chat/assistant").permitAll()
                                    .requestMatchers("/api/s3/**").permitAll()
                                    .requestMatchers(byPassPath).permitAll()
                                    .requestMatchers("/read/**").hasAuthority("SCOPE_read")
                                    .requestMatchers("/write/**").hasAuthority("SCOPE_write")
                                    .requestMatchers("/user/**").hasAnyRole("user", "admin")
                                    .requestMatchers("/admin/**").hasRole("admin")

                                    // Promotion
                                    .requestMatchers(HttpMethod.GET, "**/promotions/**").permitAll()
                                    .requestMatchers(HttpMethod.POST, "**/promotions/**").hasAnyRole("admin", "seller")
                                    .requestMatchers(HttpMethod.PUT, "**/promotions/**").hasAnyRole("admin", "seller")
                                    .requestMatchers(HttpMethod.DELETE, "**/promotions/**").hasAnyRole("admin", "seller")

                                    // Product
                                    .requestMatchers(HttpMethod.GET, "**/products/**").permitAll()
                                    .requestMatchers(HttpMethod.POST, "**/products/**").hasAnyRole("admin", "seller")
                                    .requestMatchers(HttpMethod.PUT, "**/products/**").hasAnyRole("admin", "seller")
                                    .requestMatchers(HttpMethod.DELETE, "**/products/**").hasAnyRole("admin", "seller")

                                    // AppGroup
                                    .requestMatchers(HttpMethod.GET, "**/groups/**").permitAll()
                                    .requestMatchers(HttpMethod.POST, "**/groups/**").hasAnyRole("admin", "seller")
                                    .requestMatchers(HttpMethod.PUT, "**/groups/**").hasAnyRole("admin", "seller")
                                    .requestMatchers(HttpMethod.DELETE, "**/groups/**").hasAnyRole("admin", "seller")

                                    // Category
                                    .requestMatchers(HttpMethod.GET, "**/categories/**").permitAll()
                                    .requestMatchers(HttpMethod.POST, "**/categories/**").hasAnyRole("admin", "seller")
                                    .requestMatchers(HttpMethod.PUT, "**/categories/**").hasAnyRole("admin", "seller")
                                    .requestMatchers(HttpMethod.DELETE, "**/categories/**").hasAnyRole("admin", "seller")

                                    // Account
                                    .requestMatchers(HttpMethod.GET, "**/accounts/**").hasRole("admin")
                                    .requestMatchers(HttpMethod.POST, "**/accounts/**").hasRole("admin")
                                    // TODO: Temporarily disable PUT for accounts to prevent issues with user management
                                    .requestMatchers(HttpMethod.PUT, "**/accounts/**").permitAll()
//                            .requestMatchers(HttpMethod.PUT, "**/accounts/**").hasRole("admin")
                                    .requestMatchers(HttpMethod.DELETE, "**/accounts/**").hasRole("admin")

                                    .anyRequest().authenticated()
                    )
//                    .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
                    .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())));

            // Enable if we want more details about the error
//                    .exceptionHandling(exceptions -> exceptions
//                            .authenticationEntryPoint((request, response, authException) -> {
//                                response.sendError(401, "Unauthorized");
//                            })
//                            .accessDeniedHandler((request, response, accessDeniedException) -> {
//                                response.sendError(403, "Forbidden");
//                            })
//                    );
        }
        return httpSecurity.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        final NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(publicKey).build();
        decoder.setJwtValidator(
                tokenValidator()); // hook in DelegatingOAuth2TokenValidator to JwtDecoder
        return decoder;
    }

    public OAuth2TokenValidator<Jwt> tokenValidator() {
        final List<OAuth2TokenValidator<Jwt>> validators =
                List.of(
                        new JwtTimestampValidator()
                        // new JwtIssuerValidator("http://foobar.com"),
                        // audienceValidator()
                );
        return new DelegatingOAuth2TokenValidator<>(validators);
    }

    public OAuth2TokenValidator<Jwt> audienceValidator() {
        return new JwtClaimValidator<List<String>>(
                OAuth2TokenIntrospectionClaimNames.AUD, aud -> aud.contains("foobar"));
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        final JwtGrantedAuthoritiesConverter gac = new JwtGrantedAuthoritiesConverter();
        gac.setAuthoritiesClaimName("authorities");
        gac.setAuthorityPrefix("ROLE_");

        final JwtAuthenticationConverter jac = new JwtAuthenticationConverter();
        jac.setJwtGrantedAuthoritiesConverter(gac);
        return jac;
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
