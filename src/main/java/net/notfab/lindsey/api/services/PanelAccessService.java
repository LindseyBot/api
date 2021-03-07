package net.notfab.lindsey.api.services;

import net.notfab.lindsey.api.models.ReferenceRequest;
import net.notfab.lindsey.shared.entities.panel.AccessLevel;
import net.notfab.lindsey.shared.entities.panel.PanelAccess;
import net.notfab.lindsey.shared.repositories.sql.PanelAccessRepository;
import net.notfab.lindsey.shared.rpc.FMember;
import net.notfab.lindsey.shared.services.ReferencingService;
import net.notfab.lindsey.shared.utils.Snowflake;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PanelAccessService {

    private final Snowflake snowflake;
    private final PanelAccessRepository repository;
    private final ReferencingService referencingService;

    public PanelAccessService(Snowflake snowflake, PanelAccessRepository repository,
                              ReferencingService referencingService) {
        this.snowflake = snowflake;
        this.repository = repository;
        this.referencingService = referencingService;
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

}
