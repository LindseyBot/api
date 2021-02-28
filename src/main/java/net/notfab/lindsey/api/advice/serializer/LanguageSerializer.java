package net.notfab.lindsey.api.advice.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.notfab.lindsey.shared.enums.Language;

import java.io.IOException;

public class LanguageSerializer extends JsonSerializer<Language> {

    @Override
    public void serialize(Language lang, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", lang.name());
        jsonGenerator.writeStringField("name", lang.getName());
        jsonGenerator.writeEndObject();
    }

}
