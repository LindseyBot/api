package net.notfab.lindsey.api.rest;

import net.notfab.lindsey.api.services.AutoModService;
import net.notfab.lindsey.shared.entities.profile.server.AntiAd;
import net.notfab.lindsey.shared.entities.profile.server.AutoModSettings;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("guilds/{guild}/")
public class AutoModRest {

    private final AutoModService service;

    public AutoModRest(AutoModService service) {
        this.service = service;
    }

    @GetMapping("automod")
    public AutoModSettings fetchAutoMod(@PathVariable long guild) {
        return this.service.fetchAutoMod(guild);
    }

    @PutMapping("automod")
    public AutoModSettings fetchAutoMod(@PathVariable long guild, @RequestBody AutoModSettings request) {
        return this.service.saveAutoMod(guild, request);
    }

    @GetMapping("antiad")
    public AntiAd fetchAntiAd(@PathVariable long guild) {
        return this.service.fetchAntiAd(guild);
    }

    @PutMapping("antiad")
    public AntiAd put(@PathVariable long guild, @RequestBody AntiAd request) {
        return this.service.saveAntiAd(guild, request);
    }

}
