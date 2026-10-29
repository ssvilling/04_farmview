package app.daos;

import app.dtos.SowDTO;
import app.entities.Pen;
import app.entities.Section;
import app.entities.Sow;
import app.enums.SectionName;
import app.enums.SowType;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class SowDAO implements IDAO<SowDTO, Integer> {
    private final EntityManagerFactory emf;

    public SowDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public SowDTO create(SowDTO dto) {
        try (EntityManager em = emf.createEntityManager()) {
            if (dto.getEarmark() == null || dto.getType() == null || dto.getPenId() == null) {
                throw new ApiException(400, "earmark, type and penId are required");
            }
            Pen pen = findPen(em, dto.getPenId());
            if (em.find(Sow.class, dto.getEarmark()) != null) {
                throw new ApiException(409, "A sow with earmark " + dto.getEarmark() + " already exists");
            }
            checkPlacement(dto.getType(), pen);

            Sow sow = new Sow(dto.getEarmark(), dto.getType());
            if (dto.getVirginId() != null) sow.setVirginId(dto.getVirginId());
            if (dto.getLitter() != null) sow.setLitter(dto.getLitter());
            if (dto.getTits() != null) sow.setTits(dto.getTits());
            if (dto.getDays() != null) sow.setDays(dto.getDays());

            em.getTransaction().begin();
            pen.addSow(sow);
            em.persist(sow);
            em.getTransaction().commit();

            return new SowDTO(sow);
        }
    }

    @Override
    public SowDTO read(Integer earmark) {
        try (EntityManager em = emf.createEntityManager()) {
            return new SowDTO(findSow(em, earmark));
        }
    }

    @Override
    public List<SowDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Sow> sows = em.createQuery("SELECT s FROM Sow s ORDER BY s.earmark", Sow.class).getResultList();
            return SowDTO.toDTOList(sows);
        }
    }

    public List<SowDTO> readAll(Long sectionId, SowType type) {
        try (EntityManager em = emf.createEntityManager()) {
            if (sectionId != null && em.find(Section.class, sectionId) == null) {
                throw new ApiException(404, "No section with id " + sectionId);
            }
            String jpql = "SELECT s FROM Sow s WHERE 1 = 1";
            if (sectionId != null) jpql += " AND s.pen.section.id = :sectionId";
            if (type != null) jpql += " AND s.type = :type";
            jpql += " ORDER BY s.earmark";

            TypedQuery<Sow> query = em.createQuery(jpql, Sow.class);
            if (sectionId != null) query.setParameter("sectionId", sectionId);
            if (type != null) query.setParameter("type", type);
            return SowDTO.toDTOList(query.getResultList());
        }
    }

    @Override
    public SowDTO update(SowDTO dto, Integer earmark) {
        try (EntityManager em = emf.createEntityManager()) {
            Sow sow = findSow(em, earmark);
            if (dto.getType() != null && dto.getType() != sow.getType()) {
                checkType(dto.getType(), sow.getPen());
            }

            em.getTransaction().begin();
            if (dto.getType() != null) sow.setType(dto.getType());
            if (dto.getVirginId() != null) sow.setVirginId(dto.getVirginId());
            if (dto.getLitter() != null) sow.setLitter(dto.getLitter());
            if (dto.getTits() != null) sow.setTits(dto.getTits());
            if (dto.getDays() != null) sow.setDays(dto.getDays());
            em.getTransaction().commit();

            return new SowDTO(sow);
        }
    }

    @Override
    public void delete(Integer earmark) {
        try (EntityManager em = emf.createEntityManager()) {
            Sow sow = findSow(em, earmark);

            em.getTransaction().begin();
            sow.getPen().getSows().remove(sow);
            em.remove(sow);
            em.getTransaction().commit();
        }
    }

    public SowDTO move(Integer earmark, Long penId) {
        try (EntityManager em = emf.createEntityManager()) {
            Sow sow = findSow(em, earmark);
            Pen newPen = findPen(em, penId);
            Pen oldPen = sow.getPen();
            if (oldPen.getId().equals(newPen.getId())) {
                return new SowDTO(sow);
            }
            if (newPen.isFull()) {
                throw new ApiException(409, "Pen " + newPen.getLabel() + " in " + newPen.getSection().getName() + " is full");
            }

            em.getTransaction().begin();
            oldPen.getSows().remove(sow);
            newPen.addSow(sow);
            if (sow.getType().mustBeInFarrowing() && newPen.getSection().getName() != SectionName.FARROWING) {
                sow.setType(SowType.SOW);
            }
            em.getTransaction().commit();

            return new SowDTO(sow);
        }
    }

    private void checkPlacement(SowType type, Pen pen) {
        checkType(type, pen);
        if (pen.isFull()) {
            throw new ApiException(409, "Pen " + pen.getLabel() + " in " + pen.getSection().getName() + " is full");
        }
    }

    private void checkType(SowType type, Pen pen) {
        if (type.mustBeInFarrowing() && pen.getSection().getName() != SectionName.FARROWING) {
            throw new ApiException(400, type + " can only be placed in FARROWING");
        }
    }

    private Sow findSow(EntityManager em, Integer earmark) {
        Sow sow = em.find(Sow.class, earmark);
        if (sow == null) {
            throw new ApiException(404, "No sow with earmark " + earmark);
        }
        return sow;
    }

    private Pen findPen(EntityManager em, Long id) {
        Pen pen = em.find(Pen.class, id);
        if (pen == null) {
            throw new ApiException(404, "No pen with id " + id);
        }
        return pen;
    }
}
