package net.notfab.lindsey.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiscordUser {

    private Long id;
    private String username;
    private String discrim;

}
