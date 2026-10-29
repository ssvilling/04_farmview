package app.controllers;

import app.daos.FarmDAO;
import app.dtos.FarmDTO;
import app.dtos.FarmWeatherDTO;
import app.dtos.WeatherDTO;
import app.exceptions.ApiException;
import app.services.WeatherService;
import io.javalin.http.Context;

public class WeatherController {
    private final FarmDAO farmDAO;
    private final WeatherService weatherService;

    public WeatherController(FarmDAO farmDAO, WeatherService weatherService) {
        this.farmDAO = farmDAO;
        this.weatherService = weatherService;
    }

    public void getWeather(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        FarmDTO farm = farmDAO.read(id);
        if (farm.getLatitude() == null || farm.getLongitude() == null) {
            throw new ApiException(409, "Farm " + id + " has no coordinates");
        }

        WeatherDTO weather = weatherService.getWeather(farm.getLatitude(), farm.getLongitude());
        if (weather.getHourly() == null) {
            throw new ApiException(502, "The weather service returned no hourly data");
        }
        ctx.json(new FarmWeatherDTO(farm, weather));
    }
}
