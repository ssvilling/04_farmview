package app.config;

import app.dtos.ErrorDTO;
import app.exceptions.ApiException;
import app.routes.Routes;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import io.javalin.plugin.bundled.RouteOverviewPlugin;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationConfig {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    public static Javalin startServer(int port, EntityManagerFactory emf) {
        Routes routes = new Routes(emf);

        Javalin app = Javalin.create(config -> {
            config.routes.apiBuilder(routes.getRoutes());
            config.registerPlugin(new RouteOverviewPlugin(c -> c.path = "/routes"));
            setExceptionHandling(config);
        }).start(port);
        return app;
    }

    public static void stopServer(Javalin app) {
        app.stop();
    }

    private static void setExceptionHandling(JavalinConfig config) {
        config.routes.exception(ApiException.class, (e, ctx) -> sendError(ctx, e.getCode(), e.getMessage()));

        config.routes.exception(HttpResponseException.class, (e, ctx) -> {
            ctx.status(e.getStatus());
            int status = ctx.statusCode();
            String msg = status == 404 ? "There is no endpoint " + ctx.method() + " " + ctx.path() : e.getMessage();
            sendError(ctx, status, msg);
        });

        config.routes.exception(Exception.class, (e, ctx) -> {
            logger.error("Unhandled exception on {} {}", ctx.method(), ctx.path(), e);
            sendError(ctx, 500, "Internal server error");
        });
    }

    private static void sendError(Context ctx, int status, String msg) {
        ctx.status(status);
        ctx.json(new ErrorDTO(status, msg));
    }
}
