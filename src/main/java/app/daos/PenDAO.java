package app.daos;

import app.dtos.PenDTO;
import app.entities.Pen;
import app.entities.Section;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class PenDAO {
    private final EntityManagerFactory emf;

    public PenDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public PenDTO read(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return new PenDTO(findPen(em, id));
        }
    }

    public List<PenDTO> readAllBySection(Long farmId, Long sectionId) {
        try (EntityManager em = emf.createEntityManager()) {
            checkSection(em, farmId, sectionId);
            List<Pen> pens = em.createQuery("SELECT p FROM Pen p WHERE p.section.id = :sectionId ORDER BY p.id", Pen.class)
                    .setParameter("sectionId", sectionId)
                    .getResultList();
            return PenDTO.toDTOList(pens);
        }
    }

    public List<PenDTO> readEmptyBySection(Long farmId, Long sectionId) {
        try (EntityManager em = emf.createEntityManager()) {
            checkSection(em, farmId, sectionId);
            List<Pen> pens = em.createQuery("SELECT p FROM Pen p WHERE p.section.id = :sectionId AND p.sows IS EMPTY ORDER BY p.id", Pen.class)
                    .setParameter("sectionId", sectionId)
                    .getResultList();
            return PenDTO.toDTOList(pens);
        }
    }

    private void checkSection(EntityManager em, Long farmId, Long sectionId) {
        Section section = em.find(Section.class, sectionId);
        if (section == null || !section.getFarm().getId().equals(farmId)) {
            throw new ApiException(404, "Farm " + farmId + " has no section with id " + sectionId);
        }
    }

    private Pen findPen(EntityManager em, Long id) {
        Pen pen = em.find(Pen.class, id);
        if (pen == null) {
            throw new ApiException(404, "No pen with id " + id);
        }
        return pen;
    }
}
