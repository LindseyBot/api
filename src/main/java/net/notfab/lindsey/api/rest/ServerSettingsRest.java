package net.notfab.lindsey.api.rest;

import net.notfab.lindsey.api.services.ServerSettingsService;
import net.notfab.lindsey.shared.entities.profile.ServerProfile;
import net.notfab.lindsey.shared.entities.profile.server.MusicSettings;
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

}
