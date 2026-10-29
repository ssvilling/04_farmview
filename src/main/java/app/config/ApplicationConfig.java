package app.config;

import app.dtos.ErrorDTO;
import app.exceptions.ApiException;
import app.routes.Routes;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import io.javalin.http.HttpResponseException;
import io.javalin.plugin.bundled.RouteOverviewPlugin;
import io.javalin.validation.ValidationError;
import io.javalin.validation.ValidationException;
import jakarta.persistence.EntityManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

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

        config.routes.exception(ValidationException.class, (e, ctx) -> {
            String msg = getValidationMessage(e);
            logger.warn("Validation failed on {} {}: {}", ctx.method(), ctx.path(), msg);
            sendError(ctx, 400, msg);
        });

        config.routes.exception(HttpResponseException.class, (e, ctx) -> {
            ctx.status(e.getStatus());
            int status = ctx.statusCode();
            logger.warn("{} {} -> {}", ctx.method(), ctx.path(), status);
            String msg = status == 404 ? "There is no endpoint " + ctx.method() + " " + ctx.path() : e.getMessage();
            sendError(ctx, status, msg);
        });

        config.routes.exception(Exception.class, (e, ctx) -> {
            logger.error("Unhandled exception on {} {}", ctx.method(), ctx.path(), e);
            sendError(ctx, 500, "Internal server error");
        });
    }

    private static String getValidationMessage(ValidationException e) {
        for (Map.Entry<String, List<ValidationError<Object>>> entry : e.getErrors().entrySet()) {
            for (ValidationError<Object> error : entry.getValue()) {
                return switch (error.getMessage()) {
                    case "DESERIALIZATION_FAILED" -> "The request body is not valid JSON, or has a value of the wrong type";
                    case "TYPE_CONVERSION_FAILED" -> entry.getKey() + " must be a number, not '" + error.value + "'";
                    default -> error.getMessage();
                };
            }
        }
        return "Invalid request";
    }

    private static void sendError(Context ctx, int status, String msg) {
        ctx.status(status);
        ctx.json(new ErrorDTO(status, msg));
    }
}
