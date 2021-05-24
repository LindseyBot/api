package net.notfab.lindsey.api.services;

import lombok.extern.slf4j.Slf4j;
import net.notfab.lindsey.api.advice.paging.PagedResponse;
import net.notfab.lindsey.api.advice.paging.Paginator;
import net.notfab.lindsey.api.retrofit.AuditMessage;
import net.notfab.lindsey.api.retrofit.GrayLogAPI;
import net.notfab.lindsey.api.retrofit.GrayLogResponse;
import net.notfab.lindsey.api.spring.properties.GrayLogProperties;
import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class AuditLogService implements Interceptor {

    private final GrayLogAPI api;
    private final String AUTHORIZATION;

    public AuditLogService(GrayLogProperties properties) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(properties.getUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .client(new OkHttpClient.Builder()
                        .addInterceptor(this)
                        .build())
                .build();
        this.api = retrofit.create(GrayLogAPI.class);
        this.AUTHORIZATION = Credentials.basic(properties.getToken(), "token");
    }

    public PagedResponse<AuditMessage> fetchGuild(long guildId, Paginator paginator) {
        try {
            int offset = paginator.getPage() * paginator.getLimit();
            Response<GrayLogResponse> response = this.api.relative(
                    "guild:" + guildId,
                    TimeUnit.DAYS.toSeconds(30),
                    paginator.getLimit(),
                    offset,
                    "timestamp:desc"
            ).execute();
            if (!response.isSuccessful()) {
                throw new IllegalStateException("Invalid response");
            }
            GrayLogResponse body = response.body();
            if (body == null) {
                throw new IllegalStateException("Invalid response");
            }
            PagedResponse<AuditMessage> paged = new PagedResponse<>();
            paged.setPage(paginator.getPage());
            paged.setLimit(paginator.getLimit());
            paged.setItems(body.getMessages());
            paged.setLast(offset + paged.getLimit() > body.getResults());
            return paged;
        } catch (IOException ex) {
            log.error("Failed to query graylog", ex);
            return null;
        }
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request.Builder request = chain.request().newBuilder();
        request.header("Accept", "application/json");
        request.header("Authorization", this.AUTHORIZATION);
        request.header("X-Requested-By", "Lindsey-API");
        return chain.proceed(request.build());
    }

}
