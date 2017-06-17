package ru.exam.javalab.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.exam.javalab.service.CurrentWeather;
import ru.exam.javalab.service.WeatherService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by unlim_000 on 17.06.2017.
 */

@RestController
public class WeatherController {

    private CurrentWeather currentWeather;

    @Autowired
    public WeatherController(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    @GetMapping("/api/getWeather")
    public ResponseEntity<List<String>> getWeather(@RequestParam(value = "cities[]", required = false) String[] cities) {
        if (cities == null || cities.length == 0) {
            List<String> error = new ArrayList<>();
            error.add("Nothing selected");
            return new ResponseEntity<>(error, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(currentWeather.getToClient(cities), HttpStatus.OK);
        }
    }
}
