package net.notfab.lindsey.api.repositories.sql;

import net.notfab.lindsey.shared.entities.profile.server.AutoModSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutoModRepository extends JpaRepository<AutoModSettings, Long> {
}
