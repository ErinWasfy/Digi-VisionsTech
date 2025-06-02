package com.stc.keycloakintegration.restcontroller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AuthRestController {

    @GetMapping("/whoami")
    public Map<String, Object> whoami(@AuthenticationPrincipal Jwt principal) {
        return Map.of(
                "username", principal.getClaim("preferred_username"),
                "email", principal.getClaim("email"),
                "roles", principal.getClaim("realm_access")
        );
    }
}
