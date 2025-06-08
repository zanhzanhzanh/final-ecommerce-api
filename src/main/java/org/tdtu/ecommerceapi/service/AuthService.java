package org.tdtu.ecommerceapi.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.tdtu.ecommerceapi.model.GoogleAccount;
import org.tdtu.ecommerceapi.repository.GoogleAccountRepository;
import org.tdtu.ecommerceapi.service.external.GoogleService;

import java.util.Objects;
//import org.tdtu.ecommerceapi.dto.external.response.MetaAccountResponseDTO;
//import org.tdtu.ecommerceapi.model.MetaAccount;
//import org.tdtu.ecommerceapi.repository.MetaAccountRepository;
//import org.tdtu.ecommerceapi.service.external.MetaService;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final GoogleAccountRepository googleAccountRepository;
    private final GoogleService googleService;
//    private final MetaAccountRepository metaAccountRepository;

    public String login(String email, String password) {
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(email, password));
        String token = tokenService.generateToken(authentication);
        return token;
    }

    public String loginWithGoogle(String idToken) {
        GoogleIdToken.Payload payload = googleService.verifyToken(idToken);
        GoogleAccount googleAccount =
                googleAccountRepository.findBySub(payload.getSubject()).orElse(null);
        if (Objects.isNull(googleAccount)) {
            throw new BadCredentialsException(null);
        }
        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                googleAccount.getEmail(), googleAccount.getSub()));
        String token = tokenService.generateToken(authentication);
        return token;
    }

//    public String loginWithMeta(String accessToken) {
//        MetaService metaService = new MetaService(accessToken);
//        MetaAccountResponseDTO metaAccountResponseDTO = metaService.getProfile();
//        MetaAccount metaAccount =
//                metaAccountRepository.findByMetaAccountId(metaAccountResponseDTO.getId()).orElse(null);
//        if (Objects.isNull(metaAccount)) {
//            throw new BadCredentialsException(null);
//        }
//        Authentication authentication =
//                authenticationManager.authenticate(
//                        new UsernamePasswordAuthenticationToken(
//                                metaAccount.getEmail(), metaAccount.getMetaAccountId()));
//        String token = tokenService.generateToken(authentication);
//        return token;
//    }
//
}
