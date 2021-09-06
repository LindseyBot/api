package net.notfab.lindsey.api.repositories.sql;

import net.notfab.lindsey.shared.entities.profile.ServerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerProfileRepository extends JpaRepository<ServerProfile, Long> {
}
