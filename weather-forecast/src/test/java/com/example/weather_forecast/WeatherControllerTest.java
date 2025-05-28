package com.example.weather_forecast;

import com.example.weather_forecast.controller.WeatherController;
import com.example.weather_forecast.model.WeatherResponse;
import com.example.weather_forecast.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    private WeatherResponse mockResponse;

    @BeforeEach
    public void setUp() {
        mockResponse = new WeatherResponse();
        Map<String, Object> main = new HashMap<>();
        main.put("temp", 25.0);
        main.put("humidity", 60);
        mockResponse.setMain(main);
        Map<String, Object> weatherDetails = new HashMap<>();
        weatherDetails.put("main", "Clear");
        weatherDetails.put("description", "clear sky");
        mockResponse.setWeather(Collections.singletonList(weatherDetails));
        Map<String, Object> wind = new HashMap<>();
        wind.put("speed", 5.0);
        mockResponse.setWind(wind);
        mockResponse.setName("London");
    }

    @Test
    public void testGetWeatherSuccess() throws Exception {
        Mockito.when(weatherService.getWeather(anyString())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/weather/London")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("London"))
                .andExpect(jsonPath("$.main.temp").value(25.0))
                .andExpect(jsonPath("$.main.humidity").value(60))
                .andExpect(jsonPath("$.weather[0].main").value("Clear"))
                .andExpect(jsonPath("$.wind.speed").value(5.0));
    }

    @Test
    public void testGetWeatherError() throws Exception {
        Mockito.when(weatherService.getWeather(anyString()))
                .thenThrow(new RuntimeException("City not found"));

        mockMvc.perform(get("/api/weather/UnknownCity")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error: City not found"));
    }
}
