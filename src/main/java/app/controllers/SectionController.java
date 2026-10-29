package app.controllers;

import app.daos.SectionDAO;
import app.dtos.SectionDTO;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SectionController {
    private static final Logger logger = LoggerFactory.getLogger(SectionController.class);
    private final SectionDAO sectionDAO;

    public SectionController(SectionDAO sectionDAO) {
        this.sectionDAO = sectionDAO;
    }

    public void readAll(Context ctx) {
        Long farmId = ctx.pathParamAsClass("id", Long.class).get();
        ctx.json(sectionDAO.readAllByFarm(farmId));
    }

    public void create(Context ctx) {
        Long farmId = ctx.pathParamAsClass("id", Long.class).get();
        SectionDTO dto = ctx.bodyValidator(SectionDTO.class)
                .check(s -> s.getName() != null, "Section name is required: MATING, GESTATION or FARROWING")
                .get();
        dto.setFarmId(farmId);
        SectionDTO created = sectionDAO.create(dto);
        logger.info("Section {} created on farm {}", created.getName(), farmId);
        ctx.status(201).json(created);
    }
}
