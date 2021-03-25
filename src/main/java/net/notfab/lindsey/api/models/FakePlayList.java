package net.notfab.lindsey.api.models;

import lombok.Data;
import net.notfab.lindsey.shared.entities.playlist.PlayList;
import net.notfab.lindsey.shared.enums.PlayListGenre;
import net.notfab.lindsey.shared.enums.PlayListSecurity;

@Data
public class FakePlayList {

    private long id;
    private long owner;
    private DiscordUser user;
    private String name;
    private boolean shuffle;
    private String logoUrl;
    private PlayListSecurity security;
    private PlayListGenre genre;

    public FakePlayList(PlayList playList, DiscordUser user) {
        this.setId(playList.getId());
        this.setOwner(user.getId());
        this.setName(playList.getName());
        this.setLogoUrl(playList.getLogoUrl());
        this.setSecurity(playList.getSecurity());
        this.setShuffle(playList.isShuffle());
        this.setGenre(playList.getGenre());
        this.setUser(user);
    }

}
