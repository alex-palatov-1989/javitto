package com.solar.academy.auth;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class TokenProvider {

    @Value( "${auth.token.key}" )
    private  String SECRET_KEY;

    @Value( "${auth.token.time}" )
    private  long EXPIRE_MINUTES;

    public long generate(String user, String role) throws IOException {
        var json= new JSONObject();
        var now = new Date( System.currentTimeMillis() );
        var exp = new Date( now.getTime() + EXPIRE_MINUTES*60*1000 );
        var key = Integer.toHexString(
                (int)UUID.randomUUID().getMostSignificantBits()
        );
        json
            .put("key",  key) .put("user", user).put("role", role)
            .put("EXP", exp.getTime()).put("JWT",
                Jwts.builder().setId  ( user )
                    .signWith(SignatureAlgorithm.HS256, key)
                    .compact()
            );
        System.out.println(json.toString(4));
        return service.put(json.toString());
    }
    public Jwt extractJWT(String bearer) {
        var json = read(bearer);
        if( json.has("key") && json.has("JWT"))
            return Jwts.parser()
                    .setSigningKey((String)json.get("key"))
                    .parse( (String) json.get("JWT") );
        else return null;
    }
    public boolean validate(String bearer, UserDetails user) {
        var name = extractUsername(bearer);
        return (name.equals(user.getUsername()) && !isTokenExpired(bearer));
    }

    public String extractUsername(String bearer) {
        var json = read(bearer);
        return  json.has("user") ? (String)json.get("user") : "";
    }
    public boolean isTokenExpired(String bearer) {
        var json = read(bearer);
        return json.has("EXP") ?
            ( new Date( (long)json.get("EXP") ) ) .before(new Date())
                : true;     // expired on error
    }
    @Autowired JWTService service;
    JSONObject read(String id){ return new JSONObject( service.get(id)); }
}