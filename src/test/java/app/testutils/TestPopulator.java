package app.testutils;

import app.entities.Farm;
import app.entities.Section;
import app.entities.Sow;
import app.enums.SectionName;
import app.enums.SowType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.Map;

public class TestPopulator {
    public static Map<String, Farm> populate(EntityManagerFactory emf) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createNativeQuery("TRUNCATE TABLE sow, pen, section, farm RESTART IDENTITY CASCADE").executeUpdate();
            em.getTransaction().commit();

            Farm gamlevaelde = new Farm("Gamlevælde", 55.179, 14.986);
            Farm buggemoellen = new Farm("Buggemøllen", null, null);

            Section mating = new Section(SectionName.MATING);
            Section gestation = new Section(SectionName.GESTATION);
            Section farrowing = new Section(SectionName.FARROWING);

            gamlevaelde.addSection(mating);
            gamlevaelde.addSection(gestation);
            gamlevaelde.addSection(farrowing);

            Sow sow1 = new Sow(1001, SowType.A_SOW);
            Sow sow2 = new Sow(1002, SowType.SOW);
            Sow sow3 = new Sow(1003, SowType.GILT);

            farrowing.getPens().get(0).addSow(sow1);
            farrowing.getPens().get(1).addSow(sow2);
            mating.getPens().get(0).addSow(sow3);

            em.getTransaction().begin();
            em.persist(gamlevaelde);
            em.persist(buggemoellen);
            em.persist(sow1);
            em.persist(sow2);
            em.persist(sow3);
            em.getTransaction().commit();

            return Map.of("gamlevaelde", gamlevaelde, "buggemoellen", buggemoellen);
        }
    }
}
