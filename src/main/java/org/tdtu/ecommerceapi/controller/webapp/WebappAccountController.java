package org.tdtu.ecommerceapi.controller.webapp;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tdtu.ecommerceapi.dto.admin.request.AccountRequestDTO;
import org.tdtu.ecommerceapi.dto.admin.request.GoogleAuthRequestDTO;
import org.tdtu.ecommerceapi.dto.admin.response.AccountResponseDTO;
import org.tdtu.ecommerceapi.service.AccountService;

@Tag(name = "Webapp.Account")
@RestController
@RequestMapping("/v1/webapp/account")
@RequiredArgsConstructor
public class WebappAccountController {

    private final AccountService accountService;
//    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/signup")
    public ResponseEntity<AccountResponseDTO> signup(
            @RequestBody @Valid AccountRequestDTO requestDTO) {
        AccountResponseDTO responseDTO = accountService.signup(requestDTO);
        // TODO: Needing RabbitMQ?
//        rabbitTemplate.convertAndSend("x.account-registration", "", requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/google/signup")
    public ResponseEntity<?> signupWithGoogle(@RequestBody @Valid GoogleAuthRequestDTO requestDTO) {
        AccountResponseDTO responseDTO = accountService.signupWithGoogle(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    // @PostMapping("/meta/signup")
    // public ResponseEntity<?> signupWithMeta(@RequestBody @Valid
    // MetaAuthRequestDTO requestDTO) {
    // AccountResponseDTO responseDTO = accountService.signupWithMeta(requestDTO);
    // return ResponseEntity.ok(responseDTO);
    // }
}
