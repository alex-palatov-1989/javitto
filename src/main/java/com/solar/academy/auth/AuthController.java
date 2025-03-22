package com.solar.academy.auth;

import lombok.AllArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/oauth")
@AllArgsConstructor
public class AuthController {

    AuthenticationManager   authManager;
    TokenProvider           tokenProvider;

    @PostMapping("/token")
    public ResponseEntity<?> generateToken(@RequestBody String login){
        Long token  = 0L;
        try {
            token = tokenProvider.generate(login, "userRole");
        } catch (IOException e) {
            e.printStackTrace();
            ResponseEntity.internalServerError().body( e.getMessage() );
        }

        var response = new HashMap<String, Object>();
            response.put("access_token", token);
            response.put("token_type","Bearer");
        return ResponseEntity.ok(response);
    }
}