package com.solar.academy.auth;
import com.solar.academy.config.SecurityConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenProvider tokenProvider;

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> generateToken(@RequestParam String username,
                                                             @RequestParam String password) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        var token = tokenProvider.generateToken(authentication);

            Map<String, String> response = new HashMap<>();
            response.put("access_token", token);
            response.put("token_type", "Bearer");

        return ResponseEntity.ok(response);
    }
}