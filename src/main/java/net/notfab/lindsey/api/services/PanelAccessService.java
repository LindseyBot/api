package net.notfab.lindsey.api.services;

import net.notfab.lindsey.api.models.DiscordUser;
import net.notfab.lindsey.api.models.ReferenceRequest;
import net.notfab.lindsey.shared.entities.panel.AccessLevel;
import net.notfab.lindsey.shared.entities.panel.PanelAccess;
import net.notfab.lindsey.shared.repositories.sql.PanelAccessRepository;
import net.notfab.lindsey.shared.rpc.FGuild;
import net.notfab.lindsey.shared.rpc.FMember;
import net.notfab.lindsey.shared.rpc.services.RemoteGuildsService;
import net.notfab.lindsey.shared.services.ReferencingService;
import net.notfab.lindsey.shared.utils.Snowflake;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PanelAccessService {

    private final Snowflake snowflake;
    private final PanelAccessRepository repository;
    private final ReferencingService referencingService;
    private final RemoteGuildsService remoteGuilds;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public PanelAccessService(Snowflake snowflake, PanelAccessRepository repository,
                              ReferencingService referencingService, RemoteGuildsService remoteGuilds) {
        this.snowflake = snowflake;
        this.repository = repository;
        this.referencingService = referencingService;
        this.remoteGuilds = remoteGuilds;
    }

    public List<PanelAccess> getAllAccesses(long guild) {
        return this.repository.findAllByGuild(guild);
    }

    public void delete(long id) {
        this.repository.deleteById(id);
    }

    public PanelAccess create(long guild, ReferenceRequest request) {
        FMember member = this.referencingService.getMember(request.getTicket());
        if (member == null || member.getGuildId() != guild) {
            throw new IllegalArgumentException("Invalid ticket");
        }
        Optional<PanelAccess> old = this.repository.findByUserAndGuild(member.getId(), guild);
        if (old.isPresent()) {
            return old.get();
        }
        PanelAccess access = new PanelAccess();
        access.setId(this.snowflake.next());
        access.setLevel(AccessLevel.ADMIN);
        access.setGuild(guild);
        access.setUser(member.getId());
        access.setUsername(member.getName() + "#" + member.getDiscrim());
        return this.repository.save(access);
    }

    public List<FGuild> getAllForUser(DiscordUser user) {
        List<Long> ids = this.repository.findAllByUser(user.getId()).stream()
                .map(PanelAccess::getGuild)
                .collect(Collectors.toList());
        return this.remoteGuilds.getDetails(ids, user.getId());
    }

}
