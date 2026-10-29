package app.enums;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum SectionName {
    MATING(List.of("LEFT", "RIGHT"), Integer.MAX_VALUE),
    GESTATION(numbers(7), Integer.MAX_VALUE),
    FARROWING(numbers(55), 1);

    private final List<String> penLabels;
    private final int maxSowsPerPen;

    SectionName(List<String> penLabels, int maxSowsPerPen) {
        this.penLabels = penLabels;
        this.maxSowsPerPen = maxSowsPerPen;
    }

    private static List<String> numbers(int count) {
        List<String> labels = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            labels.add(String.valueOf(i));
        }
        return labels;
    }
}
