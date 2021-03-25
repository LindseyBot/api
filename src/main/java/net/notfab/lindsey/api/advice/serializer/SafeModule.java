package net.notfab.lindsey.api.advice.serializer;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import net.notfab.lindsey.shared.entities.items.Background;
import net.notfab.lindsey.shared.entities.items.Badge;
import net.notfab.lindsey.shared.entities.items.Item;
import net.notfab.lindsey.shared.entities.items.ItemReference;
import net.notfab.lindsey.shared.entities.music.Track;
import net.notfab.lindsey.shared.entities.profile.ServerProfile;
import net.notfab.lindsey.shared.enums.Flags;
import net.notfab.lindsey.shared.enums.Language;
import org.springframework.stereotype.Component;

@Component
public class SafeModule extends SimpleModule {

    public SafeModule() {
        this.addSerializer(Flags.class, new FlagSerializer());
        this.addSerializer(Language.class, new LanguageSerializer());
        this.addSerializer(Item.class, new ItemSerializer());
        this.addSerializer(Background.class, new BackgroundSerializer());
        this.addSerializer(Badge.class, new BadgeSerializer());
        this.addSerializer(ItemReference.class, new ItemReferenceSerializer());
        this.addSerializer(Long.class, new ToStringSerializer());
        this.addSerializer(long.class, new ToStringSerializer());
        this.addSerializer(ServerProfile.class, new ServerProfileSerializer());
        this.addSerializer(Track.class, new TrackSerializer());
    }

}
