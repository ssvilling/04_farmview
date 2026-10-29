package app.daos;

import app.config.HibernateTestConfig;
import app.dtos.SowDTO;
import app.entities.Farm;
import app.entities.Section;
import app.enums.SectionName;
import app.enums.SowType;
import app.exceptions.ApiException;
import app.testutils.TestPopulator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

public class SowDAOTest {
    private static EntityManagerFactory emf;
    private static SowDAO sowDAO;
    private Map<String, Farm> farms;
    private Section mating;
    private Section gestation;
    private Section farrowing;

    @BeforeEach
    void setUp() {
        farms = TestPopulator.populate(emf);
        mating = farms.get("gamlevaelde").getSections().get(0);
        gestation = farms.get("gamlevaelde").getSections().get(1);
        farrowing = farms.get("gamlevaelde").getSections().get(2);
    }

    @BeforeAll
    static void beforeAll() {
        emf = HibernateTestConfig.getEntityManagerFactory();
        sowDAO = new SowDAO(emf);
    }

    @Test
    @DisplayName("Create a sow")
    void create() {
        SowDTO dto = new SowDTO();
        dto.setEarmark(2001);
        dto.setType(SowType.SOW);
        dto.setPenId(farrowing.getPens().get(2).getId());

        SowDTO created = sowDAO.create(dto);

        assertThat(created.getEarmark(), is(2001));
        assertThat(created.getSection(), is(SectionName.FARROWING));
        assertThat(created.getPenLabel(), is("3"));
    }

    @Test
    @DisplayName("Create a duplicate sow")
    void createDuplicateSow() {
        SowDTO dto = new SowDTO();
        dto.setEarmark(1001);
        dto.setType(SowType.SOW);
        dto.setPenId(farrowing.getPens().get(10).getId());

        ApiException e = assertThrows(ApiException.class, () -> sowDAO.create(dto));

        assertThat(e.getCode(), is(409));
    }

    @Test
    @DisplayName("Create a sow in a full pen")
    void createSowInFullPen() {
        SowDTO dto = new SowDTO();
        dto.setEarmark(2002);
        dto.setType(SowType.SOW);
        dto.setPenId(farrowing.getPens().get(0).getId());

        ApiException e = assertThrows(ApiException.class, () -> sowDAO.create(dto));

        assertThat(e.getCode(), is(409));
    }

    @Test
    @DisplayName("A_SOW cannot be placed in mating")
    void aSowCannotBeInMating() {
        SowDTO dto = new SowDTO();
        dto.setEarmark(2003);
        dto.setType(SowType.A_SOW);
        dto.setPenId(mating.getPens().get(1).getId());

        ApiException e = assertThrows(ApiException.class, () -> sowDAO.create(dto));

        assertThat(e.getCode(), is(400));
    }

    @Test
    @DisplayName("Filter sows by section and type")
    void filterSowsBySectionAndType() {
        List<SowDTO> inFarrowing = sowDAO.readAll(farrowing.getId(), null);
        List<SowDTO> gilts = sowDAO.readAll(null, SowType.GILT);

        assertThat(inFarrowing, hasSize(2));
        assertThat(gilts, hasSize(1));
    }

    @Test
    @DisplayName("Update only changes the fields that are sent")
    void updateSpecificFields() {
        SowDTO dto = new SowDTO();
        dto.setLitter(5);

        SowDTO updated = sowDAO.update(dto, 1003);

        assertThat(updated.getLitter(), is(5));
        assertThat(updated.getType(), is(SowType.GILT));
    }

    @Test
    @DisplayName("Move an A_SOW out of farrowing")
    void moveASowToAnotherSection() {
        SowDTO moved = sowDAO.move(1001, mating.getPens().get(1).getId());

        assertThat(moved.getSection(), is(SectionName.MATING));
        assertThat(moved.getType(), is(SowType.SOW));
    }

    @Test
    @DisplayName("Move a sow to a full pen")
    void moveSowToFullPen() {
        ApiException e = assertThrows(ApiException.class, () -> sowDAO.move(1003, farrowing.getPens().get(0).getId()));

        assertThat(e.getCode(), is(409));
    }
}
