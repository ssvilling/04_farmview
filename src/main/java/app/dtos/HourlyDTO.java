package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HourlyDTO {
    private List<String> time;

    @JsonProperty("temperature_2m")
    private List<Double> temperature;

    @JsonProperty("relative_humidity_2m")
    private List<Integer> humidity;

    private List<Double> rain;

    @JsonProperty("weather_code")
    private List<Integer> weatherCode;
}
