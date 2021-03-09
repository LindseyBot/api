package net.notfab.lindsey.api.services;

import net.notfab.lindsey.shared.entities.profile.ServerProfile;
import net.notfab.lindsey.shared.repositories.sql.ServerProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class ServerSettingsService {

    private final ServerProfileRepository repository;

    public ServerSettingsService(ServerProfileRepository repository) {
        this.repository = repository;
    }

    public ServerProfile fetch(long guild) {
        return this.repository.findById(guild).orElse(new ServerProfile(guild));
    }

    public ServerProfile put(long guild, ServerProfile request) {
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
        return this.repository.save(request);
    }

}
