package app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FarmWeatherDTO {
    private Long farmId;
    private String farmName;
    private List<WeatherHourDTO> hours;

    public FarmWeatherDTO(FarmDTO farm, WeatherDTO weather) {
        this.farmId = farm.getId();
        this.farmName = farm.getName();
        this.hours = new ArrayList<>();

        HourlyDTO hourly = weather.getHourly();
        for (int i = 0; i < hourly.getTime().size(); i++) {
            hours.add(new WeatherHourDTO(
                    hourly.getTime().get(i),
                    hourly.getTemperature().get(i),
                    hourly.getHumidity().get(i),
                    hourly.getRain().get(i),
                    hourly.getWeatherCode().get(i)));
        }
    }
}
