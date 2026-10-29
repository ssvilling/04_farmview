package app.services;

import app.dtos.WeatherDTO;
import app.exceptions.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class WeatherService {
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private static final String URL = "https://api.open-meteo.com/v1/forecast?latitude=%s&longitude=%s"
            + "&hourly=temperature_2m,relative_humidity_2m,weather_code,rain&timezone=Europe%%2FBerlin&forecast_days=1";

    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherDTO getWeather(double latitude, double longitude) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(String.format(URL, latitude, longitude)))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ApiException(502, "The weather service answered with status " + response.statusCode());
            }
            return objectMapper.readValue(response.body(), WeatherDTO.class);
        } catch (IOException | URISyntaxException e) {
            logger.error("Could not get the weather for {}, {}", latitude, longitude, e);
            throw new ApiException(502, "The weather service could not be reached");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ApiException(503, "The weather request was interrupted");
        }
    }
}
