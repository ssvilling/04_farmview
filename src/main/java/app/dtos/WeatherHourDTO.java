package app.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherHourDTO {
    private String time;
    private Double temperature;
    private Integer humidity;
    private Double rain;
    private Integer weatherCode;
}
