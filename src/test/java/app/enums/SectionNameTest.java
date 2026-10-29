package app.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class SectionNameTest {

    @Test
    @DisplayName("Farrowing has 55 pens with room for 1 sow each")
    void farrowing() {
        assertThat(SectionName.FARROWING.getPenLabels(), hasSize(55));
        assertThat(SectionName.FARROWING.getMaxSowsPerPen(), is(1));
    }

    @Test
    @DisplayName("Mating has the pens LEFT and RIGHT")
    void mating() {
        assertThat(SectionName.MATING.getPenLabels(), contains("LEFT", "RIGHT"));
    }

    @Test
    @DisplayName("Gestation has 7 pens")
    void gestation() {
        assertThat(SectionName.GESTATION.getPenLabels(), hasSize(7));
    }
}
