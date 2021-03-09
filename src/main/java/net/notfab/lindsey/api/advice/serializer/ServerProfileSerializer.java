package net.notfab.lindsey.api.advice.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.notfab.lindsey.shared.entities.profile.ServerProfile;

import java.io.IOException;

public class ServerProfileSerializer extends JsonSerializer<ServerProfile> {

    @Override
    public void serialize(ServerProfile profile, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("guild", String.valueOf(profile.getGuild()));
        jsonGenerator.writeStringField("prefix", profile.getPrefix());
        jsonGenerator.writeStringField("language", profile.getLanguage().name());
        jsonGenerator.writeBooleanField("keepRolesEnabled", profile.isKeepRolesEnabled());
        jsonGenerator.writeEndObject();
    }

}
