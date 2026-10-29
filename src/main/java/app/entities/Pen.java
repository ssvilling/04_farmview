package app.entities;

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
public class Pen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String label;

    @ManyToOne
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @OneToMany(mappedBy = "pen", cascade = CascadeType.REMOVE)
    private List<Sow> sows = new ArrayList<>();

    public Pen(String label) {
        this.label = label;
    }

    public void addSow(Sow sow) {
        sows.add(sow);
        sow.setPen(this);
    }

    public void removeSow(Sow sow) {
        sows.remove(sow);
        sow.setPen(null);
    }

    public boolean isFull() {
        return sows.size() >= section.getName().getMaxSowsPerPen();
    }
}
