package net.notfab.lindsey.api.rest.discord;

import net.notfab.lindsey.api.advice.security.SessionProvider;
import net.notfab.lindsey.api.models.ReferenceRequest;
import net.notfab.lindsey.api.services.PanelAccessService;
import net.notfab.lindsey.shared.entities.panel.AccessLevel;
import net.notfab.lindsey.shared.entities.panel.PanelAccess;
import net.notfab.lindsey.shared.rpc.FGuild;
import net.notfab.lindsey.shared.rpc.services.RemoteGuildsService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("guilds")
public class GuildsRest {

    private final SessionProvider sessions;
    private final RemoteGuildsService guilds;
    private final PanelAccessService panelAccessService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public GuildsRest(SessionProvider sessions, RemoteGuildsService guilds, PanelAccessService panelAccessService) {
        this.panelAccessService = panelAccessService;
        this.sessions = sessions;
        this.guilds = guilds;
    }

    @GetMapping
    public List<FGuild> findAll() {
        return this.panelAccessService.getAllForUser(this.sessions.getUser());
    }

    @GetMapping("{id}")
    public FGuild getGuild(@PathVariable("id") long id) {
        return this.guilds.getGuild(id, sessions.getUser().getId());
    }

    @GetMapping("{id}/access")
    public List<PanelAccess> getAccess(@PathVariable("id") long id) {
        return this.panelAccessService.getAllAccesses(id);
    }

    @DeleteMapping("{guild}/access/{id}")
    public void deleteAccess(@PathVariable("guild") long guild, @PathVariable("id") long id) {
        AccessLevel level = this.sessions.getAccessLevel(guild);
        if (level != AccessLevel.OWNER) {
            throw new AccessDeniedException("No permission");
        }
        this.panelAccessService.delete(id);
    }

    @PostMapping("{id}/access")
    public PanelAccess createAccess(@PathVariable("id") long guild, @RequestBody ReferenceRequest request) {
        AccessLevel level = this.sessions.getAccessLevel(guild);
        if (level != AccessLevel.OWNER) {
            throw new AccessDeniedException("No permission");
        }
        return this.panelAccessService.create(guild, request);
    }

}
