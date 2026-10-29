package app.dtos;

import app.entities.Pen;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PenDTO {
    private Long id;
    private String label;
    private Long sectionId;
    private int numberOfSows;

    public PenDTO(Pen pen) {
        this.id = pen.getId();
        this.label = pen.getLabel();
        this.sectionId = pen.getSection().getId();
        this.numberOfSows = pen.getSows().size();
    }

    public static List<PenDTO> toDTOList(List<Pen> pens) {
        return pens.stream().map(PenDTO::new).toList();
    }
}
