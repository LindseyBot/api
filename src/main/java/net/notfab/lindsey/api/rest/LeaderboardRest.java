package net.notfab.lindsey.api.rest;

import net.notfab.lindsey.api.advice.paging.KeySetPaginator;
import net.notfab.lindsey.api.advice.paging.PagedResponse;
import net.notfab.lindsey.api.models.DiscordUser;
import net.notfab.lindsey.api.models.LeaderboardEntry;
import net.notfab.lindsey.api.repositories.sql.LeaderboardRepository;
import net.notfab.lindsey.shared.entities.leaderboard.Leaderboard;
import net.notfab.lindsey.shared.entities.profile.UserProfile;
import net.notfab.lindsey.shared.entities.profile.user.UserPrivacy;
import net.notfab.lindsey.shared.enums.LeaderboardType;
import net.notfab.lindsey.shared.repositories.sql.UserPrivacyRepository;
import net.notfab.lindsey.shared.repositories.sql.UserProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public PagedResponse<LeaderboardEntry> findAll(KeySetPaginator paginator, @RequestParam("type") LeaderboardType type) {
        Page<Leaderboard> page = repository
                .findAllByIdGreaterThanAndTypeOrderByCountDesc(paginator.getCursor(), type, paginator.toPageable());
        PagedResponse<LeaderboardEntry> response = new PagedResponse<>();
        response.setLimit(page.getPageable().getPageSize());
        response.setPage(page.getNumber());
        response.setLast(page.isLast());
        response.setItems(page.stream().map(this::toEntry).collect(Collectors.toList()));
        return response;
    }

    private LeaderboardEntry toEntry(Leaderboard lb) {
        LeaderboardEntry entry = new LeaderboardEntry();
        entry.setId(lb.getId());
        entry.setCount(lb.getCount());
        entry.setType(lb.getType());

        DiscordUser user = new DiscordUser();
        user.setId(lb.getUser());
        entry.setUser(user);

        UserPrivacy privacy = userPrivacyRepository.findById(lb.getUser())
                .orElse(new UserPrivacy());
        if (privacy.isAnonymousInLeaderboards()) {
            user.setUsername("Anonymous");
            user.setDiscriminator("0000");
        } else {
            UserProfile profile = this.userProfileRepository.findById(lb.getUser())
                    .orElse(new UserProfile());
            if (profile.getName() == null) {
                user.setUsername("Ghost");
                user.setDiscriminator("0000");
            } else {
                String name = profile.getName().split("#")[0];
                String discrim = profile.getName().split("#")[1];
                user.setUsername(name);
                user.setDiscriminator(discrim);
            }
        }
        return entry;
    }

}
