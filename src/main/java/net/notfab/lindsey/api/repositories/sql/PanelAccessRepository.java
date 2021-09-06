package net.notfab.lindsey.api.repositories.sql;

import net.notfab.lindsey.shared.entities.panel.PanelAccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PanelAccessRepository extends JpaRepository<PanelAccess, Long> {

    Optional<PanelAccess> findByUserAndGuild(long user, long guild);

    List<PanelAccess> findAllByGuild(long guild);

    Optional<PanelAccess> findAllByUser(Long id);

}
