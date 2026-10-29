package app.dtos;

import app.entities.Section;
import app.enums.SectionName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectionDTO {
    private Long id;
    private SectionName name;
    private Long farmId;
    private int numberOfPens;

    public SectionDTO(Section section) {
        this.id = section.getId();
        this.name = section.getName();
        this.farmId = section.getFarm().getId();
        this.numberOfPens = section.getPens().size();
    }

    public static List<SectionDTO> toDTOList(List<Section> sections) {
        return sections.stream().map(SectionDTO::new).toList();
    }
}
