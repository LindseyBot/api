package net.notfab.lindsey.api.rest;

import net.notfab.lindsey.api.advice.paging.PagedResponse;
import net.notfab.lindsey.api.advice.paging.Paginator;
import net.notfab.lindsey.api.models.DiscordUser;
import net.notfab.lindsey.api.models.LeaderboardEntry;
import net.notfab.lindsey.api.repositories.sql.UserProfileRepository;
import net.notfab.lindsey.shared.entities.profile.UserProfile;
import net.notfab.lindsey.shared.enums.LeaderboardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("leaderboards")
public class LeaderboardRest {

    private final UserProfileRepository profiles;

    public LeaderboardRest(UserProfileRepository profiles) {
        this.profiles = profiles;
    }

    @GetMapping
    public PagedResponse<LeaderboardEntry> findAll(Paginator paginator, @RequestParam("type") LeaderboardType type) {
        Sort sort = switch (type) {
            case COOKIES -> Sort.by(Sort.Direction.DESC, "cookies");
            case SLOT_WINS -> Sort.by(Sort.Direction.DESC, "slotWins");
        };
        Page<UserProfile> page = this.profiles.findAll(paginator.toPageable(sort));
        PagedResponse<LeaderboardEntry> response = new PagedResponse<>();
        response.setLimit(page.getPageable().getPageSize());
        response.setPage(page.getNumber());
        response.setLast(page.isLast());
        response.setTotal(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setItems(page.stream().map((profile) -> this.toEntry(profile, type)).collect(Collectors.toList()));
        return response;
    }

    private LeaderboardEntry toEntry(UserProfile profile, LeaderboardType type) {
        LeaderboardEntry entry = new LeaderboardEntry();
        entry.setId(profile.getUser());
        entry.setCount(switch (type) {
            case COOKIES -> profile.getCookies();
            case SLOT_WINS -> profile.getSlotWins();
        });
        entry.setType(type);
        DiscordUser user = new DiscordUser();
        user.setId(profile.getUser());
        if (profile.getName() == null) {
            user.setUsername("Anonymous");
            user.setDiscriminator("0000");
        } else {
            String name = profile.getName().split("#")[0];
            String discriminator = profile.getName().split("#")[1];
            user.setUsername(name);
            user.setDiscriminator(discriminator);
        }
        entry.setUser(user);
        return entry;
    }

}
