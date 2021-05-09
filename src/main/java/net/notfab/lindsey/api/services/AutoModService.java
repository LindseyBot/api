package net.notfab.lindsey.api.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.lindseybot.enums.AutoModFeature;
import net.notfab.lindsey.api.repositories.sql.AutoModRepository;
import net.notfab.lindsey.shared.entities.profile.server.AntiAd;
import net.notfab.lindsey.shared.entities.profile.server.AutoModSettings;
import net.notfab.lindsey.shared.repositories.sql.server.AntiAdRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class AutoModService {

    private final StringRedisTemplate pool;
    private final ObjectMapper objectMapper;
    private final AntiAdRepository antiAdRepository;
    private final AutoModRepository autoModRepository;

    public AutoModService(
            StringRedisTemplate pool,
            ObjectMapper objectMapper,
            AntiAdRepository antiAdRepository,
            AutoModRepository autoModRepository
    ) {
        this.pool = pool;
        this.objectMapper = objectMapper;
        this.antiAdRepository = antiAdRepository;
        this.autoModRepository = autoModRepository;
    }

    public AutoModSettings fetchAutoMod(long guild) {
        return this.autoModRepository.findById(guild).orElse(new AutoModSettings(guild));
    }

    public AutoModSettings saveAutoMod(long guild, AutoModSettings request) {
        request.setGuild(guild);
        return this.autoModRepository.save(request);
    }

    private void addFeature(long guild, AutoModFeature feature) {
        String response = (String) this.pool.opsForHash()
                .get("Lindsey:AutoMod", String.valueOf(guild));
        if (response == null) {
            response = "[]";
        }
        Set<AutoModFeature> features = new HashSet<>();
        try {
            AutoModFeature[] fs = objectMapper.readValue(response, AutoModFeature[].class);
            if (fs != null && fs.length > 0) {
                Collections.addAll(features, fs);
            }
            features.add(feature);
            this.pool.opsForHash()
                    .put("Lindsey:AutoMod", String.valueOf(guild), objectMapper.writeValueAsString(features));
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize automod");
        }
    }

    private void remFeature(long guild, AutoModFeature feature) {
        String response = (String) this.pool.opsForHash()
                .get("Lindsey:AutoMod", String.valueOf(guild));
        if (response == null) {
            response = "[]";
        }
        Set<AutoModFeature> features = new HashSet<>();
        try {
            AutoModFeature[] fs = objectMapper.readValue(response, AutoModFeature[].class);
            if (fs != null && fs.length > 0) {
                Collections.addAll(features, fs);
            }
            features.remove(feature);
            if (features.isEmpty()) {
                this.pool.opsForHash().delete("Lindsey:AutoMod", String.valueOf(guild));
            } else {
                this.pool.opsForHash()
                        .put("Lindsey:AutoMod", String.valueOf(guild), objectMapper.writeValueAsString(features));
            }
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize automod");
        }
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
        if (request.isEnabled()) {
            this.addFeature(guild, AutoModFeature.ANTI_AD);
        } else {
            this.remFeature(guild, AutoModFeature.ANTI_AD);
        }
        return this.antiAdRepository.save(request);
    }

}
