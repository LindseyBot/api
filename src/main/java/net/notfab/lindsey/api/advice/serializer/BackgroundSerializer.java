package net.notfab.lindsey.api.advice.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.notfab.lindsey.shared.entities.items.Background;
import net.notfab.lindsey.shared.entities.items.Item;

import java.io.IOException;

public class BackgroundSerializer extends JsonSerializer<Background> {

    @Override
    public void serialize(Background item, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", String.valueOf(item.getId()));
        jsonGenerator.writeStringField("type", item.getType().name());
        jsonGenerator.writeStringField("name", item.getName());
        jsonGenerator.writeStringField("description", item.getDescription());
        jsonGenerator.writeStringField("asset", item.getAssetUrl());
        jsonGenerator.writeStringField("font", item.getFontColor());
        jsonGenerator.writeBooleanField("in_store", item.getInStore());
        jsonGenerator.writeBooleanField("in_market", item.getMarketable());
        jsonGenerator.writeNumberField("price", item.getStorePrice());
        jsonGenerator.writeEndObject();
    }

}
