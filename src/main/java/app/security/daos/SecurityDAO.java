package app.security.daos;

import app.exceptions.ApiException;
import app.security.entities.User;
import app.security.enums.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import dk.bugelhartmann.UserDTO;

import java.util.Set;

public class SecurityDAO {
    private final EntityManagerFactory emf;

    public SecurityDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public UserDTO createUser(String username, String password) {
        try (EntityManager em = emf.createEntityManager()) {
            if (em.find(User.class, username) != null) throw new ApiException(409, "User " + username + " already exists");
            User user = new User(username, password, Role.USER);

            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();

            return new UserDTO(user.getUsername(), Set.of(user.getRole().name()));
        }
    }

    public UserDTO getVerifiedUser(String username, String password) {
        try (EntityManager em = emf.createEntityManager()) {
            User user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) throw new ApiException(401, "Wrong username or password");
            return new UserDTO(user.getUsername(), Set.of(user.getRole().name()));
        }
    }
}
