package net.notfab.lindsey.api.retrofit;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@JsonDeserialize(using = AuditDeserializer.class)
public class AuditMessage {

    private Long guildId;
    private Level level;
    private String message;
    private Instant timestamp;

    private AuditChannel channel;
    private AuditUser user;

    public static enum Level {
        INFO,
        WARN,
        ERROR
    }

    @Data
    @AllArgsConstructor
    public static class AuditChannel {
        private Long id;
        private String name;
        private String type;
    }

    @Data
    @AllArgsConstructor
    public static class AuditUser {
        private Long id;
        private String name;
    }

    @Data
    public static class AuditMsg {
        private Long id;
        private AuditChannel channel;
        private AuditUser author;
    }

}
