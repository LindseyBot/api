package net.notfab.lindsey.api.repositories.sql;

import net.notfab.lindsey.shared.entities.leaderboard.Leaderboard;
import net.notfab.lindsey.shared.enums.LeaderboardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaderboardRepository extends JpaRepository<Leaderboard, Long> {

    List<Leaderboard> findAllByIdGreaterThanAndTypeOrderByCountDesc(long lastId, LeaderboardType type);

}
