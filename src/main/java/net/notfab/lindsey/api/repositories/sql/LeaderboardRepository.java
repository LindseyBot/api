package net.notfab.lindsey.api.repositories.sql;

import net.notfab.lindsey.shared.entities.leaderboard.Leaderboard;
import net.notfab.lindsey.shared.enums.LeaderboardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {

    Page<Leaderboard> findAllByTypeOrderByCountDesc(LeaderboardType type, Pageable pageable);

}
