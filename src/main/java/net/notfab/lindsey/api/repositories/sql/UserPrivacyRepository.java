package net.notfab.lindsey.api.repositories.sql;

import net.notfab.lindsey.shared.entities.profile.user.UserPrivacy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPrivacyRepository extends JpaRepository<UserPrivacy, Long> {
}
