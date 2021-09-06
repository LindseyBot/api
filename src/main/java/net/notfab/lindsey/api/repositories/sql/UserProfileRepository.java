package net.notfab.lindsey.api.repositories.sql;

import net.notfab.lindsey.shared.entities.profile.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
