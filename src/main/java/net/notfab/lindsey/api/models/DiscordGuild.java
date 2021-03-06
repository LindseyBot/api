package net.notfab.lindsey.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscordGuild {

    private long id;
    private String name;
    private boolean owner;
    private long permissions;

}
