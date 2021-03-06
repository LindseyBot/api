package net.notfab.lindsey.api.rest;

import net.notfab.lindsey.api.advice.security.SessionProvider;
import net.notfab.lindsey.api.models.DiscordUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserRest {

    private final SessionProvider sessionProvider;

    public UserRest(SessionProvider sessionProvider) {
        this.sessionProvider = sessionProvider;
    }

    @GetMapping("@me")
    public DiscordUser getSelf() {
        return this.sessionProvider.getUser();
    }

}
