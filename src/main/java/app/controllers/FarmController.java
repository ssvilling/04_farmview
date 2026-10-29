package app.controllers;

import app.daos.FarmDAO;
import app.dtos.FarmDTO;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FarmController {
    private static final Logger logger = LoggerFactory.getLogger(FarmController.class);
    private final FarmDAO farmDAO;

    public FarmController(FarmDAO farmDAO) {
        this.farmDAO = farmDAO;
    }

    public void readAll(Context ctx) {
        ctx.json(farmDAO.readAll());
    }

    public void read(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        ctx.json(farmDAO.read(id));
    }

    public void create(Context ctx) {
        FarmDTO dto = validateFarm(ctx);
        FarmDTO created = farmDAO.create(dto);
        logger.info("Farm created with id {}", created.getId());
        ctx.status(201).json(created);
    }

    public void update(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        ctx.json(farmDAO.update(validateFarm(ctx), id));
    }

    public void delete(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        farmDAO.delete(id);
        logger.info("Farm {} deleted", id);
        ctx.status(204);
    }

    private FarmDTO validateFarm(Context ctx) {
        return ctx.bodyValidator(FarmDTO.class)
                .check(f -> f.getName() != null && !f.getName().isBlank(), "Farm name is required")
                .check(f -> (f.getLatitude() == null) == (f.getLongitude() == null), "Send both latitude and longitude, or neither")
                .check(f -> f.getLatitude() == null || (f.getLatitude() >= -90 && f.getLatitude() <= 90), "Latitude must be between -90 and 90")
                .check(f -> f.getLongitude() == null || (f.getLongitude() >= -180 && f.getLongitude() <= 180), "Longitude must be between -180 and 180")
                .get();
    }
}