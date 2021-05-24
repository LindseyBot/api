package net.notfab.lindsey.api.retrofit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.Data;

import java.io.IOException;
import java.time.Instant;

public class AuditDeserializer extends JsonDeserializer<AuditMessage> {

    @Override
    public AuditMessage deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        Root root = p.readValueAs(Root.class);
        Message rootMsg = root.getMessage();

        AuditMessage message = new AuditMessage();
        message.setMessage(rootMsg.message);
        message.setGuildId(Long.parseLong(rootMsg.guildId));
        message.setLevel(this.parseLevel(rootMsg.level));
        message.setTimestamp(Instant.parse(rootMsg.timestamp));

        if (rootMsg.channelId != null) {
            message.setChannel(new AuditMessage.AuditChannel(
                    Long.parseLong(rootMsg.channelId), rootMsg.channelName, rootMsg.channelType));
        }
        if (rootMsg.userId != null) {
            message.setUser(new AuditMessage.AuditUser(Long.parseLong(rootMsg.userId), rootMsg.userName));
        }
        if (rootMsg.messageId != null) {
            AuditMessage.AuditMsg msg = new AuditMessage.AuditMsg();
            msg.setId(Long.parseLong(rootMsg.messageId));
            msg.setChannel(new AuditMessage.AuditChannel(
                    Long.parseLong(rootMsg.messageChannelId), rootMsg.messageChannelName, "TEXT"));
            msg.setAuthor(new AuditMessage.AuditUser(
                    Long.parseLong(rootMsg.messageAuthorId), rootMsg.messageAuthorName));
        }
        return message;
    }

    private AuditMessage.Level parseLevel(String number) {
        if ("6".equals(number)) {
            return AuditMessage.Level.INFO;
        } else if ("4".equals(number)) {
            return AuditMessage.Level.WARN;
        } else {
            return AuditMessage.Level.ERROR; // 3
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Root {
        private Message message;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Message {

        @JsonProperty("channel_name")
        private String channelName;

        private String level;

        @JsonProperty("message_id")
        private String messageId;

        @JsonProperty("message")
        private String message;

        @JsonProperty("guild")
        private String guildId;

        @JsonProperty("message_channel_name")
        private String messageChannelName;

        @JsonProperty("user_id")
        private String userId;

        @JsonProperty("message_channel_id")
        private String messageChannelId;

        @JsonProperty("message_author_id")
        private String messageAuthorId;

        @JsonProperty("message_author")
        private String messageAuthorName;

        @JsonProperty("channel_type")
        private String channelType;

        @JsonProperty("user")
        private String userName;

        @JsonProperty("channel_id")
        private String channelId;

        @JsonProperty("timestamp")
        private String timestamp;

    }

}
