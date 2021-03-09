package net.notfab.lindsey.api.rest;

import net.notfab.lindsey.api.services.ServerSettingsService;
import net.notfab.lindsey.shared.entities.profile.ServerProfile;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("guilds/{guild}/settings")
public class ServerSettingsRest {

    private final ServerSettingsService service;

    public ServerSettingsRest(ServerSettingsService service) {
        this.service = service;
    }

    @GetMapping
    public ServerProfile fetch(@PathVariable("guild") long guild) {
        return this.service.fetch(guild);
    }

    @PutMapping
    public ServerProfile put(@PathVariable("guild") long guild, @RequestBody ServerProfile request) {
        return this.service.put(guild, request);
    }

}
