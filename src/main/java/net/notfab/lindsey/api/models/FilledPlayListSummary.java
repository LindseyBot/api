package net.notfab.lindsey.api.models;

import lombok.Data;
import net.notfab.lindsey.shared.entities.playlist.PlayListSummary;
import net.notfab.lindsey.shared.enums.PlayListGenre;

@Data
public class FilledPlayListSummary implements PlayListSummary {

    private long id;
    private String name;
    private String logo;
    private PlayListGenre genre;
    private long votes;
    private long songs;

    public FilledPlayListSummary(PlayListSummary summary) {
        this.id = summary.getId();
        this.name = summary.getName();
        this.logo = summary.getLogo();
        this.genre = summary.getGenre();
        this.votes = summary.getVotes();
    }

}
