package net.notfab.lindsey.api.models;

import lombok.Data;
import net.notfab.lindsey.shared.enums.LeaderboardType;

@Data
public class LeaderboardEntry {

    private long id;
    private DiscordUser user;

    private double count;
    private LeaderboardType type;

}
