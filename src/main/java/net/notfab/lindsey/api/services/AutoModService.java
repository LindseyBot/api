package net.notfab.lindsey.api.services;

import net.notfab.lindsey.shared.entities.profile.server.AntiAd;
import net.notfab.lindsey.shared.repositories.sql.server.AntiAdRepository;
import org.springframework.stereotype.Service;

@Service
public class AutoModService {

    private final AntiAdRepository antiAdRepository;

    public AutoModService(AntiAdRepository antiAdRepository) {
        this.antiAdRepository = antiAdRepository;
    }

    public AntiAd fetchAntiAd(long guild) {
        return this.antiAdRepository.findById(guild).orElse(new AntiAd(guild));
    }

    public AntiAd saveAntiAd(long guild, AntiAd request) {
        if (request.getStrikes() < 1) {
            throw new IllegalArgumentException("Strikes value is too low");
        }
        if (request.getStrikes() > 10) {
            throw new IllegalArgumentException("Strikes value is too high");
        }
        request.setGuild(guild);
        return this.antiAdRepository.save(request);
    }

}
