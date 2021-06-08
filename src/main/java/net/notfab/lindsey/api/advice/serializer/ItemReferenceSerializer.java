package net.notfab.lindsey.api.advice.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.notfab.lindsey.shared.entities.items.ItemReference;

import java.io.IOException;

public class ItemReferenceSerializer extends JsonSerializer<ItemReference> {

    @Override
    public void serialize(ItemReference reference, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", String.valueOf(reference.getId()));
        jsonGenerator.writeStringField("type", reference.getType().name());
        jsonGenerator.writeStringField("item", String.valueOf(reference.getItem().getId()));
        jsonGenerator.writeStringField("owner", String.valueOf(reference.getOwner()));
        jsonGenerator.writeNumberField("count", reference.getCount());
        jsonGenerator.writeEndObject();
    }

}
