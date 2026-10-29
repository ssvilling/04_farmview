package app.dtos;

import app.entities.Sow;
import app.enums.SectionName;
import app.enums.SowType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SowDTO {
    private Integer earmark;
    private SowType type;
    private Long penId;
    private String penLabel;
    private SectionName section;
    private Integer virginId;
    private Integer litter;
    private Integer tits;
    private Integer days;

    public SowDTO(Sow sow) {
        this.earmark = sow.getEarmark();
        this.type = sow.getType();
        this.penId = sow.getPen().getId();
        this.penLabel = sow.getPen().getLabel();
        this.section = sow.getPen().getSection().getName();
        this.virginId = sow.getVirginId();
        this.litter = sow.getLitter();
        this.tits = sow.getTits();
        this.days = sow.getDays();
    }

    public static List<SowDTO> toDTOList(List<Sow> sows) {
        return sows.stream().map(SowDTO::new).toList();
    }
}
