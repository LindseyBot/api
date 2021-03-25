package net.notfab.lindsey.api.advice.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.notfab.lindsey.shared.entities.music.Track;

import java.io.IOException;

public class TrackSerializer extends JsonSerializer<Track> {

    @Override
    public void serialize(Track track, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("code", track.getCode());
        jsonGenerator.writeStringField("title", track.getTitle());
        jsonGenerator.writeStringField("author", track.getAuthor());
        jsonGenerator.writeNumberField("duration", track.getDuration());
        jsonGenerator.writeBooleanField("stream", track.isStream());
        jsonGenerator.writeStringField("source", track.getSource().name());
        jsonGenerator.writeEndObject();
    }

}
