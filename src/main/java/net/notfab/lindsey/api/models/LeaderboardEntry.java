package net.notfab.lindsey.api.models;

import lombok.Data;
import net.notfab.lindsey.shared.enums.LeaderboardType;

@Data
public class LeaderboardEntry {

    private User user;

    private double count;
    private LeaderboardType type;

}
