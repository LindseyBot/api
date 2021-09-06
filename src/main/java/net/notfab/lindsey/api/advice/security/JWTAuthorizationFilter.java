package net.notfab.lindsey.api.advice.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import net.notfab.lindsey.api.advice.RestfulErrorAdviser;
import net.notfab.lindsey.api.models.DiscordUser;
import net.notfab.lindsey.api.spring.SecurityService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final SecurityService securityService;
    private final ObjectMapper objectMapper;

    public JWTAuthorizationFilter(AuthenticationManager authManager, SecurityService securityService,
                                  ObjectMapper objectMapper) {
        super(authManager);
        this.securityService = securityService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String header = req.getHeader(this.securityService.HEADER_NAME);
        if (header == null || !header.startsWith(this.securityService.TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }
        try {
            UsernamePasswordAuthenticationToken authentication =
                    this.getAuthentication(header.substring(this.securityService.TOKEN_PREFIX.length()));
            if (authentication == null) {
                chain.doFilter(req, res);
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | BadCredentialsException ex) {
            RestfulErrorAdviser.handleFilterException(res, ex, 401);
            return;
        }
        chain.doFilter(req, res);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(this.securityService.JWT_KEY.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        if (claims != null) {
            String subject = claims.getSubject();
            DiscordUser user;
            try {
                user = this.objectMapper.readValue(subject, DiscordUser.class);
            } catch (JsonProcessingException e) {
                throw new BadCredentialsException("Failed to authenticate");
            }
            return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        } else {
            return null;
        }
    }

}
