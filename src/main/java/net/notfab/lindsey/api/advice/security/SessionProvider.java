package net.notfab.lindsey.api.advice.security;

import net.notfab.lindsey.api.models.DiscordUser;
import net.notfab.lindsey.api.repositories.sql.PanelAccessRepository;
import net.notfab.lindsey.shared.entities.panel.AccessLevel;
import net.notfab.lindsey.shared.entities.panel.PanelAccess;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SessionProvider {

    private final PanelAccessRepository repository;

    public SessionProvider(PanelAccessRepository repository) {
        this.repository = repository;
    }

    public boolean hasSession() {
        return SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal() != null;
    }

    public DiscordUser getUser() {
        return (DiscordUser) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
    }

    public boolean hasAccess(long guild) {
        return repository.findByUserAndGuild(this.getUser().getId(), guild)
                .isPresent();
    }

    public AccessLevel getAccessLevel(long guild) {
        Optional<PanelAccess> oAccess = repository.findByUserAndGuild(this.getUser().getId(), guild);
        if (oAccess.isEmpty()) {
            return null;
        }
        return oAccess.get().getLevel();
    }

}
