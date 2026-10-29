package app.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class SowTypeTest {

    @Test
    @DisplayName("A_SOW and CARE_SOW must be in farrowing")
    void mustBeInFarrowing() {
        assertThat(SowType.A_SOW.mustBeInFarrowing(), is(true));
        assertThat(SowType.CARE_SOW.mustBeInFarrowing(), is(true));
    }

    @Test
    @DisplayName("GILT and SOW can be anywhere")
    void canBeAnywhere() {
        assertThat(SowType.GILT.mustBeInFarrowing(), is(false));
        assertThat(SowType.SOW.mustBeInFarrowing(), is(false));
    }
}
