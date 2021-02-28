package net.notfab.lindsey.api.advice.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.notfab.lindsey.shared.enums.Flags;

import java.io.IOException;

public class FlagSerializer extends JsonSerializer<Flags> {

    @Override
    public void serialize(Flags flag, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", flag.name());
        jsonGenerator.writeStringField("name", flag.getName());
        jsonGenerator.writeEndObject();
    }

}
