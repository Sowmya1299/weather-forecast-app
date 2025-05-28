package com.example.weather_forecast;
import com.example.weather_forecast.model.WeatherResponse;
import com.example.weather_forecast.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherService weatherService;
    private static final String API_KEY = "dummyApiKey";
    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(weatherService, "apiKey", API_KEY);
        ReflectionTestUtils.setField(weatherService, "apiUrl", BASE_URL);
    }

    private WeatherResponse stubResponse(String city) {
        WeatherResponse r = new WeatherResponse();
        r.setName(city);
        Map<String, Object> main = new HashMap<>();
        main.put("temp", 25.0);
        main.put("humidity", 60);
        r.setMain(main);
        Map<String, Object> wind = new HashMap<>();
        wind.put("speed", 4.5);
        r.setWind(wind);
        List<Map<String, Object>> weatherList = new ArrayList<>();
        Map<String, Object> cond = new HashMap<>();
        cond.put("main", "Clear");
        weatherList.add(cond);
        r.setWeather(weatherList);
        return r;
    }

    private String expectedUrl(String city) {
        return BASE_URL + "?q=" + URLEncoder.encode(city, StandardCharsets.UTF_8)
                + "&appid=" + API_KEY + "&units=metric";
    }

    @Test
    void getWeather_whenCityNotFound_throwsCustomException() {
        when(restTemplate.getForObject(expectedUrl("Atlantis"), WeatherResponse.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        assertThrows(
                Exception.class,
                () -> weatherService.getWeather("Atlantis")
        );
    }

    @Test
    void getWeather_whenRestTemplateFails_throwsServiceException() {
        when(restTemplate.getForObject(expectedUrl("Tokyo"), WeatherResponse.class))
                .thenThrow(new RestClientException("boom"));
        assertThrows(
                Exception.class,
                () -> weatherService.getWeather("Tokyo")
        );
    }

    @Test
    void getWeather_whenNullReturned_throwsServiceException() {
        when(restTemplate.getForObject(expectedUrl("Paris"), WeatherResponse.class))
                .thenReturn(null);
        assertThrows(
                Exception.class,
                () -> weatherService.getWeather("Paris")
        );
    }
}