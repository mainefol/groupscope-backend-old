package org.groupscope.util;

import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    public HttpCookie createAccessTokenCookie(String token, Long duration) {
        return ResponseCookie.from("accessToken", token)
                .maxAge(duration)
                .httpOnly(true)
                .path("/")
                .build();
    }

    public HttpCookie createRefreshTokenCookie(String token, Long duration) {
        return ResponseCookie.from("refreshToken", token)
                .maxAge(duration)
                .httpOnly(true)
                .path("/")
                .build();
    }

    public HttpCookie deleteAccessTokenCookie() {
        return ResponseCookie.from("accessToken", "").maxAge(0).httpOnly(true).path("/").build();
    }
}
