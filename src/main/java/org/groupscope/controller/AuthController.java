package org.groupscope.controller;

import lombok.extern.slf4j.Slf4j;
import org.groupscope.entity.Provider;
import org.groupscope.security.JwtProvider;
import org.groupscope.security.dto.*;
import org.groupscope.security.entity.RefreshToken;
import org.groupscope.security.entity.User;
import org.groupscope.security.services.RefreshTokenService;
import org.groupscope.security.services.auth.UserService;
import org.groupscope.security.services.oauth2.OAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
public class AuthController {
    private final UserService userService;

    private final OAuth2UserService OAuth2UserService;

    private final JwtProvider jwtProvider;

    private final RefreshTokenService refreshTokenService;

    @Autowired
    public AuthController(UserService userService,
                          OAuth2UserService OAuth2UserService,
                          JwtProvider jwtProvider,
                          RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.OAuth2UserService = OAuth2UserService;
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> registerUser(@RequestBody @Valid RegistrationRequest request) {
        try {
            // Checking for user existing
            if (userService.findByLogin(request.getLogin()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }

            if(!request.isValid()) {
                log.info(request + " not valid");
                return ResponseEntity.badRequest().build();
            }

            // Save new user
            User user = userService.saveUser(request.toUser(), request, Provider.LOCAL);
            if (user != null) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PostMapping("/auth")
    public ResponseEntity<AuthResponse> auth(@RequestBody @Valid AuthRequest request) {
        try {
            User user = userService.findByLoginAndPassword(request.getLogin(), request.getPassword());
            if (user != null) {
                String refreshToken = refreshTokenService.createOrUpdateRefreshToken(user, false).getToken();
                String jwtToken = jwtProvider.generateToken(user.getLogin());
                return ResponseEntity.ok(new AuthResponse(jwtToken, refreshToken));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PostMapping("/oauth2")
    public ResponseEntity<AuthResponse> auth(@RequestBody @Valid OAuth2Request request) {
        try {
            User user = OAuth2UserService.loginOAuthGoogle(request);
            if (user != null) {
                String refreshToken = refreshTokenService.createOrUpdateRefreshToken(user, false).getToken();
                String jwtToken = jwtProvider.generateToken(user.getLogin());
                return ResponseEntity.ok(new AuthResponse(jwtToken, refreshToken));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshJwtToken(@RequestBody @Valid TokenRefreshRequest refreshRequest) {
        try {
            RefreshToken refreshTokenObj = refreshTokenService.findByToken(refreshRequest.getRefreshToken());

            if(refreshTokenObj != null) {
                User user = refreshTokenObj.getUser();
                String refreshToken = refreshTokenService.createOrUpdateRefreshToken(user, true).getToken();
                String jwtToken = jwtProvider.generateToken(user.getLogin());
                return ResponseEntity.ok(new AuthResponse(jwtToken, refreshToken));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }
    }

    @GetMapping("/hi")
    public ResponseEntity<Void> handleHeadRequest() {
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
