package net.notfab.lindsey.api.services;

import net.notfab.lindsey.api.advice.paging.PagedResponse;
import net.notfab.lindsey.api.advice.paging.Paginator;
import net.notfab.lindsey.api.retrofit.AuditMessage;
import net.notfab.lindsey.shared.entities.profile.ServerProfile;
import net.notfab.lindsey.shared.entities.profile.server.BetterEmbedsSettings;
import net.notfab.lindsey.shared.entities.profile.server.MusicSettings;
import net.notfab.lindsey.shared.entities.profile.server.StarboardSettings;
import net.notfab.lindsey.shared.repositories.sql.BetterEmbedSettingsRepository;
import net.notfab.lindsey.shared.repositories.sql.ServerProfileRepository;
import net.notfab.lindsey.shared.repositories.sql.server.MusicSettingsRepository;
import net.notfab.lindsey.shared.repositories.sql.server.StarboardSettingsRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ServerSettingsService {

    private final ServerProfileRepository settingsRepository;
    private final MusicSettingsRepository musicRepository;
    private final StarboardSettingsRepository starboardRepository;
    private final BetterEmbedSettingsRepository embedsRepository;
    private final StringRedisTemplate redis;
    private final AuditLogService audit;

    public ServerSettingsService(ServerProfileRepository settingsRepository,
                                 MusicSettingsRepository musicRepository,
                                 StarboardSettingsRepository starboardRepository,
                                 BetterEmbedSettingsRepository embedsRepository,
                                 StringRedisTemplate redis, AuditLogService audit) {
        this.settingsRepository = settingsRepository;
        this.musicRepository = musicRepository;
        this.starboardRepository = starboardRepository;
        this.embedsRepository = embedsRepository;
        this.redis = redis;
        this.audit = audit;
    }

    public ServerProfile fetchSettings(long guild) {
        ServerProfile profile = this.settingsRepository.findById(guild)
                .orElse(new ServerProfile(guild));
        String ignoredKey = "Lindsey:Ignore:" + guild;
        Boolean hasKey = redis.hasKey(ignoredKey);
        if (hasKey != null && hasKey) {
            profile.setIgnoredChannels(redis.opsForSet().members(ignoredKey));
        }
        return profile;
    }

    public ServerProfile putSettings(long guild, ServerProfile request) {
        if (request.getLanguage() == null) {
            throw new IllegalArgumentException("Invalid language");
        }
        if (request.getPrefix() != null) {
            if (request.getPrefix().contains(" ")) {
                throw new IllegalArgumentException("Invalid prefix");
            }
            if (request.getPrefix().isBlank() || request.getPrefix().equals("L!")) {
                request.setPrefix(null);
            }
        }
        if (request.getIgnoredChannels() != null) {
            String ignoredKey = "Lindsey:Ignore:" + guild;
            redis.delete(ignoredKey);
            if (!request.getIgnoredChannels().isEmpty()) {
                redis.opsForSet().add(ignoredKey, request.getIgnoredChannels().toArray(new String[0]));
            }
        }
        request.setGuild(guild);
        ServerProfile response = this.settingsRepository.save(request);
        response.setIgnoredChannels(request.getIgnoredChannels());
        return response;
    }

    public MusicSettings fetchMusic(long guild) {
        return this.musicRepository.findById(guild).orElse(new MusicSettings(guild));
    }

    public MusicSettings putMusic(long guild, MusicSettings request) {
        if (request.getLogChannel() == null) {
            request.setLogTracks(false);
        } else if (!request.isLogTracks()) {
            request.setLogChannel(null);
        }
        MusicSettings old = this.fetchMusic(guild);
        request.setPosition(old.getPosition());
        request.setGuild(guild);
        return this.musicRepository.save(request);
    }

    public StarboardSettings fetchStarboard(long guild) {
        return this.starboardRepository.findById(guild).orElse(new StarboardSettings(guild));
    }

    public StarboardSettings putStarboard(long guild, StarboardSettings request) {
        if (request.getChannel() == null) {
            request.setEnabled(false);
        } else if (!request.isEnabled()) {
            request.setChannel(null);
        }
        if (request.getMinStars() < 1 || request.getMinStars() > 10) {
            throw new IllegalArgumentException("Invalid minimum star count");
        }
        request.setGuild(guild);
        return this.starboardRepository.save(request);
    }

    public BetterEmbedsSettings fetchEmbeds(long guild) {
        return this.embedsRepository.findById(guild).orElse(new BetterEmbedsSettings(guild));
    }

    public BetterEmbedsSettings putEmbeds(long guild, BetterEmbedsSettings request) {
        request.setGuild(guild);
        return this.embedsRepository.save(request);
    }

    public PagedResponse<AuditMessage> fetchLogs(long guild, Paginator paginator) {
        PagedResponse<AuditMessage> paged = this.audit.fetchGuild(guild, paginator);
        if (paged == null) {
            throw new IllegalStateException("Failed to fetch logs");
        }
        return paged;
    }

}
