package net.notfab.lindsey.api.rest;

import net.notfab.lindsey.api.services.ServerSettingsService;
import net.notfab.lindsey.shared.entities.profile.ServerProfile;
import net.notfab.lindsey.shared.entities.profile.server.BetterEmbedsSettings;
import net.notfab.lindsey.shared.entities.profile.server.MusicSettings;
import net.notfab.lindsey.shared.entities.profile.server.StarboardSettings;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("guilds/{guild}/")
public class ServerSettingsRest {

    private final ServerSettingsService service;

    public ServerSettingsRest(ServerSettingsService service) {
        this.service = service;
    }

    @GetMapping("settings")
    public ServerProfile fetchSettings(@PathVariable long guild) {
        return this.service.fetchSettings(guild);
    }

    @PutMapping("settings")
    public ServerProfile putSettings(@PathVariable long guild, @RequestBody ServerProfile request) {
        return this.service.putSettings(guild, request);
    }

    @GetMapping("music")
    public MusicSettings fetchMusic(@PathVariable long guild) {
        return this.service.fetchMusic(guild);
    }

    @PutMapping("music")
    public MusicSettings putMusic(@PathVariable long guild, @RequestBody MusicSettings request) {
        return this.service.putMusic(guild, request);
    }

    @GetMapping("starboard")
    public StarboardSettings fetchStarboard(@PathVariable long guild) {
        return this.service.fetchStarboard(guild);
    }

    @PutMapping("starboard")
    public StarboardSettings putStarboard(@PathVariable long guild, @RequestBody StarboardSettings request) {
        return this.service.putStarboard(guild, request);
    }

    @GetMapping("embeds")
    public BetterEmbedsSettings fetchEmbeds(@PathVariable long guild) {
        return this.service.fetchEmbeds(guild);
    }

    @PutMapping("embeds")
    public BetterEmbedsSettings putEmbeds(@PathVariable long guild, @RequestBody BetterEmbedsSettings request) {
        return this.service.putEmbeds(guild, request);
    }

}
