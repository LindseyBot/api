package net.notfab.lindsey.api.rest;

import net.notfab.lindsey.api.services.AutoModService;
import net.notfab.lindsey.shared.entities.profile.server.AntiAd;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("guilds/{guild}/")
public class AutoModRest {

    private final AutoModService service;

    public AutoModRest(AutoModService service) {
        this.service = service;
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
