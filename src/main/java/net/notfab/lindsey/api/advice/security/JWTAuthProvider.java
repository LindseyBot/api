package net.notfab.lindsey.api.advice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.notfab.lindsey.api.models.DiscordUser;
import net.notfab.lindsey.api.models.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class JWTAuthProvider implements AuthenticationProvider {

    private final ObjectMapper objectMapper;
    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public JWTAuthProvider(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getName();
        User user = this.getFromDiscord(token);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        return new UsernamePasswordAuthenticationToken(user, token, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private User getFromDiscord(String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://discord.com/api/v8/users/@me"))
                .header("User-Agent", "Lindsey/1.0")
                .header("Authorization", "Bearer " + token)
                .build();
        try {
            HttpResponse<InputStream> response = this.client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() != 200) {
                throw new BadCredentialsException("Failed to authenticate");
            }
            DiscordUser user = this.objectMapper.readValue(response.body(), DiscordUser.class);
            return new User(user.getId(), user.getUsername() + "#" + user.getDiscriminator());
        } catch (IOException | InterruptedException e) {
            throw new BadCredentialsException("Error during authentication");
        }
    }

}
