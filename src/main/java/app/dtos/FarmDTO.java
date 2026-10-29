package app.dtos;

import app.entities.Farm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FarmDTO {
    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;

    public FarmDTO(Farm farm) {
        this.id = farm.getId();
        this.name = farm.getName();
        this.latitude = farm.getLatitude();
        this.longitude = farm.getLongitude();
    }

    public static List<FarmDTO> toDTOList(List<Farm> farms) {
        return farms.stream().map(FarmDTO::new).toList();
    }
}