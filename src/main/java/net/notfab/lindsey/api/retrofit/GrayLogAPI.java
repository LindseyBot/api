package net.notfab.lindsey.api.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GrayLogAPI {

    @GET("api/search/universal/relative")
    Call<GrayLogResponse> relative(
            @Query("query") String query,
            @Query("range") long range,
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("sort") String sort
    );

}
