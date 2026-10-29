package app.entities;

import app.enums.SowType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Sow {
    @Id
    private Integer earmark;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SowType type;

    private int virginId;
    private int litter;
    private int tits;
    private int days;

    @ManyToOne
    @JoinColumn(name = "pen_id", nullable = false)
    private Pen pen;

    public Sow(Integer earmark, SowType type) {
        this.earmark = earmark;
        this.type = type;
    }
}