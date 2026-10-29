package app.daos;

import app.dtos.FarmDTO;
import app.entities.Farm;
import app.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class FarmDAO implements IDAO<FarmDTO, Long> {
    private final EntityManagerFactory emf;

    public FarmDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public FarmDTO create(FarmDTO dto) {
        try (EntityManager em = emf.createEntityManager()) {
            Farm farm = new Farm(dto.getName(), dto.getLatitude(), dto.getLongitude());

            em.getTransaction().begin();
            em.persist(farm);
            em.getTransaction().commit();

            return new FarmDTO(farm);
        }
    }

    @Override
    public FarmDTO read(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            return new FarmDTO(findFarm(em, id));
        }
    }

    @Override
    public List<FarmDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            List<Farm> farms = em.createQuery("SELECT f FROM Farm f ORDER BY f.id", Farm.class).getResultList();
            return FarmDTO.toDTOList(farms);
        }
    }

    @Override
    public FarmDTO update(FarmDTO dto, Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Farm farm = findFarm(em, id);

            em.getTransaction().begin();
            farm.setName(dto.getName());
            farm.setLatitude(dto.getLatitude());
            farm.setLongitude(dto.getLongitude());
            em.getTransaction().commit();

            return new FarmDTO(farm);
        }
    }

    @Override
    public void delete(Long id) {
        try (EntityManager em = emf.createEntityManager()) {
            Farm farm = findFarm(em, id);

            em.getTransaction().begin();
            em.remove(farm);
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
}
