package app.controllers;

import app.daos.PenDAO;
import io.javalin.http.Context;

public class PenController {
    private final PenDAO penDAO;

    public PenController(PenDAO penDAO) {
        this.penDAO = penDAO;
    }

    public void readAll(Context ctx) {
        Long farmId = ctx.pathParamAsClass("id", Long.class).get();
        Long sectionId = ctx.pathParamAsClass("sectionId", Long.class).get();
        ctx.json(penDAO.readAllBySection(farmId, sectionId));
    }

    public void readEmpty(Context ctx) {
        Long farmId = ctx.pathParamAsClass("id", Long.class).get();
        Long sectionId = ctx.pathParamAsClass("sectionId", Long.class).get();
        ctx.json(penDAO.readEmptyBySection(farmId, sectionId));
    }
}
