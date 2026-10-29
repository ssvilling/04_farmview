package app.controllers;

import app.daos.SowDAO;
import app.dtos.SowDTO;
import app.enums.SowType;
import app.exceptions.ApiException;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SowController {
    private static final Logger logger = LoggerFactory.getLogger(SowController.class);
    private final SowDAO sowDAO;

    public SowController(SowDAO sowDAO) {
        this.sowDAO = sowDAO;
    }

    public void readAll(Context ctx) {
        Long sectionId = ctx.queryParamAsClass("section", Long.class).getOrNull();
        SowType type = null;
        if (ctx.queryParam("type") != null) {
            type = parseType(ctx.queryParam("type"));
        }
        ctx.json(sowDAO.readAll(sectionId, type));
    }

    public void read(Context ctx) {
        Integer earmark = ctx.pathParamAsClass("earmark", Integer.class).get();
        ctx.json(sowDAO.read(earmark));
    }

    public void create(Context ctx) {
        SowDTO dto = ctx.bodyValidator(SowDTO.class)
                .check(s -> s.getEarmark() != null && s.getEarmark() > 0, "Earmark must be a positive number")
                .check(s -> s.getType() != null, "Type is required: GILT, SOW, A_SOW or CARE_SOW")
                .check(s -> s.getPenId() != null, "Pen id is required")
                .check(this::noNegativeValues, "Values cannot be negative")
                .get();
        SowDTO created = sowDAO.create(dto);
        logger.info("Sow {} created in pen {}", created.getEarmark(), created.getPenId());
        ctx.status(201).json(created);
    }

    public void update(Context ctx) {
        Integer earmark = ctx.pathParamAsClass("earmark", Integer.class).get();
        SowDTO dto = ctx.bodyValidator(SowDTO.class)
                .check(s -> s.getType() != null || s.getVirginId() != null || s.getLitter() != null
                        || s.getTits() != null || s.getDays() != null,
                        "Send at least one of type, virginId, litter, tits or days")
                .check(this::noNegativeValues, "Values cannot be negative")
                .get();
        ctx.json(sowDAO.update(dto, earmark));
    }

    public void delete(Context ctx) {
        Integer earmark = ctx.pathParamAsClass("earmark", Integer.class).get();
        sowDAO.delete(earmark);
        logger.info("Sow {} deleted", earmark);
        ctx.status(204);
    }

    public void move(Context ctx) {
        Integer earmark = ctx.pathParamAsClass("earmark", Integer.class).get();
        SowDTO dto = ctx.bodyValidator(SowDTO.class)
                .check(s -> s.getPenId() != null, "Pen id is required")
                .get();
        SowDTO moved = sowDAO.move(earmark, dto.getPenId());
        logger.info("Sow {} moved to pen {}", earmark, moved.getPenId());
        ctx.json(moved);
    }

    private SowType parseType(String type) {
        try {
            return SowType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "type must be one of GILT, SOW, A_SOW or CARE_SOW");
        }
    }

    private boolean noNegativeValues(SowDTO s) {
        return isNotNegative(s.getVirginId()) && isNotNegative(s.getLitter())
                && isNotNegative(s.getTits()) && isNotNegative(s.getDays());
    }

    private boolean isNotNegative(Integer value) {
        return value == null || value >= 0;
    }
}
