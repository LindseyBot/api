package net.notfab.lindsey.api.rest;

import net.notfab.lindsey.api.advice.paging.KeySetPaginator;
import net.notfab.lindsey.api.models.LeaderboardEntry;
import net.notfab.lindsey.api.models.User;
import net.notfab.lindsey.api.repositories.sql.LeaderboardRepository;
import net.notfab.lindsey.shared.entities.leaderboard.Leaderboard;
import net.notfab.lindsey.shared.entities.profile.UserProfile;
import net.notfab.lindsey.shared.entities.profile.user.UserPrivacy;
import net.notfab.lindsey.shared.enums.LeaderboardType;
import net.notfab.lindsey.shared.repositories.sql.UserPrivacyRepository;
import net.notfab.lindsey.shared.repositories.sql.UserProfileRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("leaderboards")
public class LeaderboardRest {

    private final LeaderboardRepository repository;
    private final UserProfileRepository userProfileRepository;
    private final UserPrivacyRepository userPrivacyRepository;

    public LeaderboardRest(LeaderboardRepository repository,
                           UserProfileRepository userProfileRepository,
                           UserPrivacyRepository userPrivacyRepository) {
        this.repository = repository;
        this.userProfileRepository = userProfileRepository;
        this.userPrivacyRepository = userPrivacyRepository;
    }

    @GetMapping
    public List<LeaderboardEntry> findAll(KeySetPaginator paginator, @RequestParam("type") LeaderboardType type) {
        return repository.findAllByIdGreaterThanAndTypeOrderByCountDesc(paginator.getLast(), type)
                .stream().map(this::toEntry).collect(Collectors.toList());
    }

    private LeaderboardEntry toEntry(Leaderboard lb) {
        LeaderboardEntry entry = new LeaderboardEntry();
        entry.setCount(lb.getCount());
        entry.setType(lb.getType());

        User user = new User();
        user.setId(lb.getUser());
        entry.setUser(user);

        UserPrivacy privacy = userPrivacyRepository.findById(lb.getUser())
                .orElse(new UserPrivacy());
        if (privacy.isAnonymousInLeaderboards()) {
            user.setName("Anonymous#0000");
        } else {
            UserProfile profile = this.userProfileRepository.findById(lb.getUser())
                    .orElse(new UserProfile());
            if (profile.getName() == null) {
                user.setName("A Ghost#0000");
            } else {
                user.setName(profile.getName());
            }
        }
        return entry;
    }

}
