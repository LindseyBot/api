package net.notfab.lindsey.api.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import net.notfab.lindsey.api.models.DiscordUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class SecurityService {

    public final String HEADER_NAME = "Authorization";
    public final String TOKEN_PREFIX = "Bearer ";
    public final long EXPIRATION_TIME = TimeUnit.HOURS.toMillis(6);

    public final String JWT_KEY; // 64 chars
    private final ObjectMapper objectMapper;

    public SecurityService(@Value("${app.security.jwt}") String jwt, ObjectMapper objectMapper) {
        this.JWT_KEY = jwt;
        this.objectMapper = objectMapper;
    }

    public String generateToken(DiscordUser user) {
        Claims claims;
        try {
            claims = Jwts.claims()
                    .setSubject(this.objectMapper.writeValueAsString(user));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to generate token");
        }
        Date exp = new Date(System.currentTimeMillis() + EXPIRATION_TIME);
        Key key = Keys.hmacShaKeyFor(JWT_KEY.getBytes());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(exp)
                .compact();
    }

}
