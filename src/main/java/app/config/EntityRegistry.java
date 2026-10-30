package app.config;

import app.entities.Farm;
import app.entities.Pen;
import app.entities.Section;
import app.entities.Sow;
import app.security.entities.User;
import org.hibernate.cfg.Configuration;

final class EntityRegistry {

    private EntityRegistry() {}

    static void registerEntities(Configuration configuration) {
        configuration.addAnnotatedClass(Farm.class);
        configuration.addAnnotatedClass(Section.class);
        configuration.addAnnotatedClass(Pen.class);
        configuration.addAnnotatedClass(Sow.class);
        configuration.addAnnotatedClass(User.class);
    }
}