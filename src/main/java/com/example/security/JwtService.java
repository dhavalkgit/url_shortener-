package com.example.security;

import com.example.model.User;
import com.example.repository.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.expiry}")
    private int expiry;

    @Value("${jwt.secrete}")
    private String secrete;

    private final UserRepo userRepo;

    public JwtService(UserRepo userRepo){
        this.userRepo=userRepo;
    }

    public SecretKey getKeys(){
        return Keys.hmacShaKeyFor(secrete.getBytes(StandardCharsets.UTF_8));
    }

    /**
     *  It takes input as JWT token and extract payload for JWT token and return it.
     */
    private Claims extractAllPayload(String token){
        return Jwts.parser()
                .verifyWith(getKeys())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * It used to extract single claim form token based on claim name
     */
    private String extractClaim(String token, String claimName){
        Claims claims = extractAllPayload(token);
        return (String) claims.get(claimName);
    }
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver){
        Claims claims = extractAllPayload(token);
        return claimResolver.apply(claims);
    }

    /**
     *  check token is expired or not
     */
    private Boolean tokenExpired(String token){
        Date exp = (Date) extractClaim(token, Claims::getExpiration);
        return new Date().before(exp);
    }

    public Boolean isTokenValid(String token, String email){
        Claims claims = extractAllPayload(token);
        return email.equals(claims.getSubject()) && tokenExpired(token);
    }

    /**
     *  The method is generate JWT token
     */
    public String createToken(String username, String type){
        Date now = new Date();
        Date exp = new Date(now.getTime()+ expiry *1000L);

        return  Jwts.builder()
                .claims(getPayload(username,type))
                .issuedAt(now)
                .expiration(exp)
                .subject(username)
                .signWith(getKeys())
                .compact();
    }

    private Map<String,Object> getPayload(String username, String type){
        Optional<User> user = userRepo.findByEmail(username);
        Map<String,Object> payload = new HashMap<>();
        if(user.isPresent()){
            payload.put("name",user.get().getName());
            payload.put("creationDate",user.get().getCreatedAt());
            payload.put("id", user.get().getId());
        }
        payload.put("type",type);
        return payload;
    }

    public String getEmail(String token){
        Claims claims = extractAllPayload(token);
        return claims.getSubject();
    }
}
