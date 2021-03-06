package net.notfab.lindsey.api.rest.discord;

import net.notfab.lindsey.api.advice.security.SessionProvider;
import net.notfab.lindsey.shared.rpc.DGuild;
import net.notfab.lindsey.shared.rpc.services.RemoteGuilds;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("guilds")
public class GuildsRest {

    private final SessionProvider sessions;
    private final RemoteGuilds guilds;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public GuildsRest(SessionProvider sessions, RemoteGuilds guilds) {
        this.sessions = sessions;
        this.guilds = guilds;
    }

    @GetMapping("{id}")
    public DGuild getGuild(@PathVariable("id") long id) {
        return this.guilds.getGuild(id, sessions.getUser().getId());
    }

}
