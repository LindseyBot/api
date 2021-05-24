package net.notfab.lindsey.api.spring.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.logs")
public class GrayLogProperties {

    private String url;
    private String token;

}
