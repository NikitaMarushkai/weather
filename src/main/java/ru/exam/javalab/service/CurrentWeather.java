package ru.exam.javalab.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.stereotype.Service;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.java8.Java8CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import ru.exam.javalab.model.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class CurrentWeather {


    private static final String APPID = "da45a437794f46095faab5cb1438bb4c";
    private WeatherService weatherService;

    public CurrentWeather() {
        Retrofit retrofit = createRetrofit();
        weatherService = retrofit.create(WeatherService.class);
    }


    public List<String> getToClient(String[] cities) {
        final List<String> results = new ArrayList<>(cities.length);
        Arrays.stream(cities).parallel()
                .forEach(city -> {
                    CompletableFuture<Response<Data>> responseBody = weatherService.getWeather(city, APPID, "");
                    responseBody.thenAccept(response -> {
                        if (response.isSuccessful()) {
                            Data data = response.body();
                            results.add(data.parse());
                        } else {
                            try {
                                results.add("Error: " + this.parseErrorResponse(response) + "; sent value: " + city);
                            } catch (IOException e) {
                                results.add("Error: failed to get error body");
                            }
                        }
                    }).join();
                });

        return results;
    }

    private String parseErrorResponse(Response<?> response) throws IOException {
        JsonParser parser = new JsonParser();
        JsonElement jsonElement = parser.parse(response.errorBody().string());
        JsonObject rootObject = jsonElement.getAsJsonObject();
        return rootObject.get("message").getAsString();
    }

    private static Retrofit createRetrofit() {

        return new Retrofit.Builder()
                .client(new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .addInterceptor(new HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                        .build())
                .baseUrl("http://api.openweathermap.org")
                .addCallAdapterFactory(Java8CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

}

