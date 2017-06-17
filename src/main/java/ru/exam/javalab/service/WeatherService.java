package ru.exam.javalab.service;

import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.exam.javalab.model.Data;

import java.util.concurrent.CompletableFuture;

/**
 * Created by unlim_000 on 17.06.2017.
 */
public interface WeatherService {
    @FormUrlEncoded
    @POST("/data/2.5/weather")
    CompletableFuture<Response<Data>> getWeather(@Query("q") String city, @Query("APPID") String key, @Field("") String empty);
}
