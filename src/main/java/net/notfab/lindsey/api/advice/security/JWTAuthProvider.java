package net.notfab.lindsey.api.advice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.notfab.lindsey.api.models.DiscordGuild;
import net.notfab.lindsey.api.models.DiscordUser;
import net.notfab.lindsey.shared.entities.panel.AccessLevel;
import net.notfab.lindsey.shared.entities.panel.PanelAccess;
import net.notfab.lindsey.shared.repositories.sql.PanelAccessRepository;
import net.notfab.lindsey.shared.utils.Snowflake;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class JWTAuthProvider implements AuthenticationProvider {

    private final Snowflake snowflake;
    private final ObjectMapper objectMapper;
    private final PanelAccessRepository repository;
    private final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public JWTAuthProvider(Snowflake snowflake, ObjectMapper objectMapper, PanelAccessRepository repository) {
        this.snowflake = snowflake;
        this.objectMapper = objectMapper;
        this.repository = repository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getName();
        DiscordUser user = this.getFromDiscord(token);
        // Update panel access
        this.updateAccess(user, this.findGuilds(token));
        return new UsernamePasswordAuthenticationToken(user, token, new ArrayList<>());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private DiscordUser getFromDiscord(String token) {
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
            return this.objectMapper.readValue(response.body(), DiscordUser.class);
        } catch (IOException | InterruptedException e) {
            throw new BadCredentialsException("Error during authentication");
        }
    }

    private void updateAccess(DiscordUser user, List<DiscordGuild> guilds) {
        guilds.parallelStream().forEach(guild -> {
            AccessLevel level = this.getAccessLevel(guild);
            if (level == null) {
                return;
            }
            Optional<PanelAccess> oAccess = this.repository.findByUserAndGuild(user.getId(), guild.getId());
            PanelAccess access;
            if (oAccess.isPresent()) {
                access = oAccess.get();
            } else {
                access = new PanelAccess();
                access.setId(this.snowflake.next());
                access.setGuild(guild.getId());
                access.setUser(user.getId());
            }
            access.setUsername(user.getUsername() + "#" + user.getDiscrim());
            access.setLevel(level);
            this.repository.save(access);
        });
    }

    private List<DiscordGuild> findGuilds(String token) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("https://discord.com/api/v8/users/@me/guilds"))
                .header("User-Agent", "Lindsey/1.0")
                .header("Authorization", "Bearer " + token)
                .build();
        try {
            HttpResponse<InputStream> response = this.client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            if (response.statusCode() != 200) {
                throw new BadCredentialsException("Failed to authenticate");
            }
            DiscordGuild[] guild = this.objectMapper
                    .readValue(response.body(), DiscordGuild[].class);
            return Arrays.asList(guild);
        } catch (IOException | InterruptedException e) {
            throw new BadCredentialsException("Error during authentication");
        }
    }

    private AccessLevel getAccessLevel(DiscordGuild guild) {
        if (guild.isOwner()) {
            return AccessLevel.OWNER;
        }
        long perms = guild.getPermissions();
        return (perms & 0x8) == 0x8 ? AccessLevel.ADMIN : null;
    }

}
