package app.daos;

import app.dtos.SectionDTO;
import app.entities.Farm;
import app.entities.Section;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class SectionDAO {
    private final EntityManagerFactory emf;

    public SectionDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public SectionDTO create(SectionDTO dto) {
        try (EntityManager em = emf.createEntityManager()) {
            Farm farm = findFarm(em, dto.getFarmId());
            if (farm.hasSection(dto.getName())) {
                throw new ApiException(409, farm.getName() + " already has a " + dto.getName() + " section");
            }
            Section section = new Section(dto.getName());

            em.getTransaction().begin();
            farm.addSection(section);
            em.persist(section);
            em.getTransaction().commit();

            return new SectionDTO(section);
        }
    }

    public SectionDTO read(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return new SectionDTO(findSection(em, id));
        }
    }

    public List<SectionDTO> readAllByFarm(Long farmId) {
        try (EntityManager em = emf.createEntityManager()) {
            findFarm(em, farmId);
            List<Section> sections = em.createQuery("SELECT s FROM Section s WHERE s.farm.id = :farmId ORDER BY s.id", Section.class)
                    .setParameter("farmId", farmId)
                    .getResultList();
            return SectionDTO.toDTOList(sections);
        }
    }

    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Section section = findSection(em, id);

            em.getTransaction().begin();
            section.getFarm().getSections().remove(section);
            em.remove(section);
            em.getTransaction().commit();
        }
    }

    private Farm findFarm(EntityManager em, Long id) {
        Farm farm = em.find(Farm.class, id);
        if (farm == null) {
            throw new ApiException(404, "No farm with id " + id);
        }
        return farm;
    }

    private Section findSection(EntityManager em, Long id) {
        Section section = em.find(Section.class, id);
        if (section == null) {
            throw new ApiException(404, "No section with id " + id);
        }
        return section;
    }
}
