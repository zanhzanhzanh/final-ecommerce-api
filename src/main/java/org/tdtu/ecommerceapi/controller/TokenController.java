package org.tdtu.ecommerceapi.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.tdtu.ecommerceapi.dto.Token;
import org.tdtu.ecommerceapi.dto.admin.request.AuthRequestDTO;
import org.tdtu.ecommerceapi.dto.admin.request.GoogleAuthRequestDTO;
import org.tdtu.ecommerceapi.dto.admin.response.AuthResponseDTO;
import org.tdtu.ecommerceapi.service.AuthService;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TokenController {

    private final AuthService authService;

    @GetMapping("/token/details")
    public Token getToken(JwtAuthenticationToken jwtToken) {
        return new Token(jwtToken.getToken(), jwtToken.getAuthorities());
    }

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @io.swagger.v3.oas.annotations.media.Content(
                    mediaType = "application/json",
                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                            value = "{ \"email\": \"admin@gmail.com\", \"password\": \"password\" }"
                    )
            )
    )
    @PostMapping("/token")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequestDTO requestDTO) {
        String token = authService.login(requestDTO.getEmail(), requestDTO.getPassword());
        return ResponseEntity.ok(new AuthResponseDTO(token));
    }

    @PostMapping("/google/token")
    public ResponseEntity<?> loginWithGoogle(@RequestBody @Valid GoogleAuthRequestDTO
                                                     requestDTO) {
        try {
            String token = authService.loginWithGoogle(requestDTO.getIdToken());
            return ResponseEntity.ok(new AuthResponseDTO(token));
        } catch (BadCredentialsException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    //  @PostMapping("/meta/token")
    //  public ResponseEntity<?> loginWithMeta(@RequestBody @Valid MetaAuthRequestDTO requestDTO) {
    //    try {
    //      String token = authService.loginWithMeta(requestDTO.getAccessToken());
    //      return ResponseEntity.ok(new AuthResponseDTO(token));
    //    } catch (BadCredentialsException exception) {
    //      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    //    }
    //  }
    //
}
