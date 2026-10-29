package app.routes;

import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {
    public Routes(EntityManagerFactory emf) {}

    public EndpointGroup getRoutes() {
        return () -> path("/api", () -> {
            get("/health", ctx -> ctx.json(Map.of("status", "ok")));
        });
    }
}
