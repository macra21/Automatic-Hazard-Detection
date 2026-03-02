package Utils;

import Exceptions.DatabaseException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Utility class for managing the Hibernate {@link SessionFactory}.
 * <p>
 *     This class is responsible for configuring Hibernate and providing a singleton
 *     instance of the {@link SessionFactory}, which is expensive to create and should be shared.
 * </p>
 */
public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    /**
     * Builds the {@link SessionFactory} from the hibernate.cfg.xml configuration file.
     * @return the initialized SessionFactory
     * @throws DatabaseException if the creation of the SessionFactory fails
     */
    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }

    /**
     * Returns the singleton instance of the {@link SessionFactory}.
     * @return the SessionFactory instance
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Closes the {@link SessionFactory} and releases all resources.
     * Should be called when the application is shutting down.
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}
