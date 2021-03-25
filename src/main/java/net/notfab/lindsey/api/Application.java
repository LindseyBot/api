package net.notfab.lindsey.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.notfab.lindsey.shared.repositories.sql.CuratorRepository;
import net.notfab.lindsey.shared.repositories.sql.TrackRepository;
import net.notfab.lindsey.shared.services.PlayListService;
import net.notfab.lindsey.shared.services.ReferencingService;
import net.notfab.lindsey.shared.utils.Snowflake;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    Snowflake snowflake() {
        return new Snowflake();
    }

    @Bean
    ReferencingService referencingService(StringRedisTemplate redis, ObjectMapper objectMapper) {
        return new ReferencingService(objectMapper, redis);
    }

    @Bean
    public PlayListService playListService(StringRedisTemplate redis, TrackRepository repository, CuratorRepository curators) {
        return new PlayListService(repository, redis, curators);
    }

}
