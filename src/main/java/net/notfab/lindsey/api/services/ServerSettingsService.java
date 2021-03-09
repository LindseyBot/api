package net.notfab.lindsey.api.services;

import net.notfab.lindsey.shared.entities.profile.ServerProfile;
import net.notfab.lindsey.shared.entities.profile.server.MusicSettings;
import net.notfab.lindsey.shared.repositories.sql.ServerProfileRepository;
import net.notfab.lindsey.shared.repositories.sql.server.MusicSettingsRepository;
import org.springframework.stereotype.Service;

@Service
public class ServerSettingsService {

    private final ServerProfileRepository settingsRepository;
    private final MusicSettingsRepository musicRepository;

    public ServerSettingsService(ServerProfileRepository settingsRepository, MusicSettingsRepository musicRepository) {
        this.settingsRepository = settingsRepository;
        this.musicRepository = musicRepository;
    }

    public ServerProfile fetchSettings(long guild) {
        return this.settingsRepository.findById(guild).orElse(new ServerProfile(guild));
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
        request.setGuild(guild);
        return this.settingsRepository.save(request);
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

}
