package app.routes;

import app.controllers.FarmController;
import app.controllers.PenController;
import app.controllers.SectionController;
import app.controllers.SowController;
import app.controllers.WeatherController;
import app.daos.FarmDAO;
import app.daos.PenDAO;
import app.daos.SectionDAO;
import app.daos.SowDAO;
import app.services.WeatherService;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    private final FarmController farmController;
    private final SectionController sectionController;
    private final PenController penController;
    private final SowController sowController;
    private final WeatherController weatherController;

    public Routes(EntityManagerFactory emf) {
        FarmDAO farmDAO = new FarmDAO(emf);
        farmController = new FarmController(farmDAO);
        sectionController = new SectionController(new SectionDAO(emf));
        penController = new PenController(new PenDAO(emf));
        sowController = new SowController(new SowDAO(emf));
        weatherController = new WeatherController(farmDAO, new WeatherService());
    }

    public EndpointGroup getRoutes() {
        return () -> path("/api", () -> {
            get("/health", ctx -> ctx.json(Map.of("status", "ok")));
            path("/farms", farmRoutes());
            path("/sows", sowRoutes());
        });
    }

    private EndpointGroup farmRoutes() {
        return () -> {
            get(farmController::readAll);
            post(farmController::create);
            path("/{id}", () -> {
                get(farmController::read);
                put(farmController::update);
                delete(farmController::delete);
                get("/weather", weatherController::getWeather);
                path("/sections", () -> {
                    get(sectionController::readAll);
                    post(sectionController::create);
                    get("/{sectionId}/pens", penController::readAll);
                    get("/{sectionId}/pens/empty", penController::readEmpty);
                });
            });
        };
    }

    private EndpointGroup sowRoutes() {
        return () -> {
            get(sowController::readAll);
            post(sowController::create);
            path("/{earmark}", () -> {
                get(sowController::read);
                put(sowController::update);
                delete(sowController::delete);
                put("/move", sowController::move);
            });
        };
    }
}
