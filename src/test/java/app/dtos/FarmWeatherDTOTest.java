package app.dtos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class FarmWeatherDTOTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String json = """
            {
              "latitude": 55.18,
              "longitude": 14.98,
              "generationtime_ms": 0.05,
              "timezone": "Europe/Berlin",
              "hourly_units": {"time": "iso8601", "temperature_2m": "°C"},
              "hourly": {
                "time": ["2026-10-29T00:00", "2026-10-29T01:00"],
                "temperature_2m": [8.1, 7.9],
                "relative_humidity_2m": [91, 92],
                "weather_code": [3, 2],
                "rain": [0.0, 0.2]
              }
            }
            """;

    @Test
    @DisplayName("the answer from Open-Meteo is read into WeatherDTO")
    void readWeather() throws JsonProcessingException {
        WeatherDTO weather = objectMapper.readValue(json, WeatherDTO.class);

        assertThat(weather.getHourly().getTime(), hasSize(2));
        assertThat(weather.getHourly().getTemperature(), contains(8.1, 7.9));
        assertThat(weather.getHourly().getHumidity(), contains(91, 92));
        assertThat(weather.getHourly().getWeatherCode(), contains(3, 2));
    }

    @Test
    @DisplayName("FarmWeatherDTO makes one hour per time")
    void makeHours() throws JsonProcessingException {
        WeatherDTO weather = objectMapper.readValue(json, WeatherDTO.class);
        FarmDTO farm = new FarmDTO(1L, "Gamlevælde", 55.179, 14.986);

        FarmWeatherDTO farmWeather = new FarmWeatherDTO(farm, weather);

        assertThat(farmWeather.getFarmName(), is("Gamlevælde"));
        assertThat(farmWeather.getHours(), hasSize(2));
        assertThat(farmWeather.getHours().get(1).getTime(), is("2026-10-29T01:00"));
        assertThat(farmWeather.getHours().get(1).getRain(), is(0.2));
    }
}
