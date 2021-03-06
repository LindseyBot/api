package net.notfab.lindsey.api.advice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.notfab.lindsey.api.models.DiscordUser;
import net.notfab.lindsey.api.spring.SecurityService;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final SecurityService securityService;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, SecurityService securityService) {
        this.authenticationManager = authenticationManager;
        this.securityService = securityService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException {
        try {
            AuthRequest credentials = new ObjectMapper().readValue(req.getInputStream(), AuthRequest.class);
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    credentials.getToken(), null, new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
                                            Authentication auth) {
        DiscordUser user = (DiscordUser) auth.getPrincipal();
        String token = this.securityService.generateToken(user);
        try {
            JSONObject object = new JSONObject();
            object.put("token", token);
            res.getWriter().write(object.toString());
            res.addHeader("Content-Type", "application/json");
        } catch (IOException ex) {
            logger.error("Failed to generate JWT token", ex);
        }
    }

}
