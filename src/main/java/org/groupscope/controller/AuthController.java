package org.groupscope.controller;

import lombok.extern.slf4j.Slf4j;
import org.groupscope.dto.LearnerDTO;
import org.groupscope.entity.Provider;
import org.groupscope.security.JwtProvider;
import org.groupscope.security.auth.CustomUser;
import org.groupscope.security.auth.CustomUserService;
import org.groupscope.security.dto.AuthRequest;
import org.groupscope.security.dto.AuthResponse;
import org.groupscope.security.dto.OAuth2Request;
import org.groupscope.security.dto.RegistrationRequest;
import org.groupscope.security.oauth2.CustomOAuth2UserService;
import org.groupscope.services.GroupScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class AuthController {
    private final CustomUserService customUserService;

    private final CustomOAuth2UserService customOAuth2UserService;

    private final JwtProvider jwtProvider;

    @Autowired
    public AuthController(CustomUserService customUserService,
                          CustomOAuth2UserService customOAuth2UserService,
                          JwtProvider jwtProvider) {
        this.customUserService = customUserService;
        this.customOAuth2UserService = customOAuth2UserService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> registerUser(@RequestBody @Valid RegistrationRequest request) {
        if(customUserService.findByLogin(request.getLogin()) == null) {
            CustomUser user = customUserService.saveUser(request.toUser(), request, Provider.LOCAL);
            if (user != null) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(@RequestBody @Valid AuthRequest request) {
        CustomUser user = customUserService.findByLoginAndPassword(request.getLogin(), request.getPassword());
        if(user != null) {
            String token = jwtProvider.generateToken(user.getLogin());
            AuthResponse authResponse = new AuthResponse(token);
            return ResponseEntity.ok(authResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/oauth2")
    public ResponseEntity<AuthResponse> auth(@RequestBody @Valid OAuth2Request request) {
        CustomUser user = customOAuth2UserService.loginOAuthGoogle(request);
        if(user != null) {
            String token = jwtProvider.generateToken(user.getLogin());
            AuthResponse authResponse = new AuthResponse(token);
            return ResponseEntity.ok(authResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
