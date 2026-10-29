package app.enums;

public enum SowType {
    GILT,
    SOW,
    A_SOW,
    CARE_SOW;

    public boolean mustBeInFarrowing() {
        return this == A_SOW || this == CARE_SOW;
    }
}
