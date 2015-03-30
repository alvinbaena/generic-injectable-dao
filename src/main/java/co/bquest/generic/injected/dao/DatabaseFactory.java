/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.bquest.generic.injected.dao;

import co.bquest.generic.injected.dao.jpa.HibernateJpaDao;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import org.atteo.classindex.ClassIndex;

/**
 *
 * @author bquest
 */
@Stateless
public class DatabaseFactory {

    private static final Logger LOG = Logger.getLogger(DatabaseFactory.class.getName());

    @Produces
    public HibernateJpaDao newInstance(InjectionPoint ip) {
        if (ip.getAnnotated().isAnnotationPresent(Dao.class)) {
            Dao ann = ip.getAnnotated().getAnnotation(Dao.class);

            if (ann.managerName().isEmpty()) {
                EntityManager defaultEm = getDefaultEntityManager();

                LOG.log(Level.INFO, "Generating new instance of JpaDao for entity [{0}] (default) ", ann.entityClass().getSimpleName());
                return new HibernateJpaDao(ann.entityClass(), defaultEm);
            } else {
                EntityManager lookup = getEntityManager(ann.managerName());

                LOG.log(Level.INFO, "Generating new instance of JpaDao for entity [{0}] ", ann.entityClass().getSimpleName());
                return new HibernateJpaDao(ann.entityClass(), lookup);
            }
        }

        throw new IllegalStateException("Never should have happened");
    }

    private EntityManager getDefaultEntityManager() {
        try {
            for (Class<?> annotated : ClassIndex.getSubclasses(DaoDataSource.class)) {
                if (annotated.isAnnotationPresent(Manager.class)) {
                    Manager m = annotated.getAnnotation(Manager.class);

                    if (m.defaultManager()) {
                        InitialContext ic = new InitialContext();
                        DaoDataSource dds = (DaoDataSource) ic.lookup(m.lookup());
                        return dds.getEntityManager();
                    }
                } else {
                    throw new NotImplementedException("Subclass of DaoDataSource.class does not have the @Manager annotation");
                }
            }
        } catch (NamingException ex) {
            Logger.getLogger(DatabaseFactory.class.getName()).log(Level.SEVERE, null, ex);
        }

        throw new RuntimeException("No default @Manager specified");
    }

    private EntityManager getEntityManager(String managerName) {
        try {
            for (Class<?> annotated : ClassIndex.getSubclasses(DaoDataSource.class)) {
                if (annotated.isAnnotationPresent(Manager.class)) {
                    Manager m = annotated.getAnnotation(Manager.class);

                    if (m.name().equals(managerName)) {
                        InitialContext ic = new InitialContext();
                        DaoDataSource dds = (DaoDataSource) ic.lookup(m.lookup());
                        return dds.getEntityManager();
                    }
                } else {
                    throw new NotImplementedException("Subclass of DaoDataSource.class does not have the @Manager annotation");
                }
            }

            throw new RuntimeException("@Manager with name [" + managerName + "] not found");
        } catch (NamingException ex) {
            Logger.getLogger(DatabaseFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
