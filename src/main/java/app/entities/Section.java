package app.entities;

import app.enums.SectionName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"farm_id", "name"}))
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SectionName name;

    @ManyToOne
    @JoinColumn(name = "farm_id", nullable = false)
    private Farm farm;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pen> pens = new ArrayList<>();

    public Section(SectionName name) {
        this.name = name;
        for (String label : name.getPenLabels()) {
            addPen(new Pen(label));
        }
    }

    public void addPen(Pen pen) {
        pens.add(pen);
        pen.setSection(this);
    }
}
