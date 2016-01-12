/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gammapeit.generic.injected.dao;

import com.gammapeit.generic.injected.dao.exception.GenericDaoException;
import com.gammapeit.generic.injected.dao.exception.MissingAnnotationException;
import com.gammapeit.generic.injected.dao.jpa.BaseJpaDao;
import com.gammapeit.generic.injected.dao.jpa.JpaDao;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import org.atteo.classindex.ClassIndex;

/**
 *
 * @author alvinbaena
 */
public class GenericDaoFactory {

    private static final Logger LOG = Logger.getLogger(GenericDaoFactory.class.getName());

    @Produces
    public BaseJpaDao newInstance(InjectionPoint ip) {
        if (ip.getAnnotated().isAnnotationPresent(GenericDao.class)) {
            GenericDao ann = ip.getAnnotated().getAnnotation(GenericDao.class);

            if (ann.managerName().isEmpty()) {
                EntityManager defaultEm = getDefaultEntityManager();

                LOG.log(Level.FINE, "Generating new instance of JpaDao for entity [{0}] with default manager", ann.entityClass().getSimpleName());
                return new JpaDao(ann.entityClass(), defaultEm);
            } else {
                EntityManager lookup = getEntityManager(ann.managerName());

                LOG.log(Level.FINE, "Generating new instance of JpaDao for entity [{0}] with non default manager [{1}]", new Object[]{ann.entityClass().getSimpleName(), ann.managerName()});
                return new JpaDao(ann.entityClass(), lookup);
            }
        }

        throw new IllegalStateException("Cannot inject GenericDao class without @GenericDao annotation");
    }

    private EntityManager getDefaultEntityManager() {
        try {
            for (Class<?> annotated : ClassIndex.getSubclasses(DaoDataSource.class)) {
                if (annotated.isAnnotationPresent(Manager.class)) {
                    Manager m = annotated.getAnnotation(Manager.class);

                    if (m.defaultManager()) {
                        LOG.log(Level.FINE, "Looking up default @Manager [{0}]", m.lookup());
                        InitialContext ic = new InitialContext();
                        DaoDataSource dds = (DaoDataSource) ic.lookup(m.lookup());
                        return dds.getEntityManager();
                    }
                } else {
                    throw new MissingAnnotationException("Subclass of DaoDataSource.class does not have the @Manager annotation");
                }
            }
        } catch (NamingException ex) {
            throw new GenericDaoException(ex);
        }

        throw new RuntimeException("No default @Manager specified");
    }

    private EntityManager getEntityManager(String managerName) {
        try {
            for (Class<?> annotated : ClassIndex.getSubclasses(DaoDataSource.class)) {
                if (annotated.isAnnotationPresent(Manager.class)) {
                    Manager m = annotated.getAnnotation(Manager.class);

                    if (m.name().equals(managerName)) {
                        LOG.log(Level.FINE, "Looking up [{0}] @Manager [{1}]", new Object[]{m.name(), m.lookup()});
                        InitialContext ic = new InitialContext();
                        DaoDataSource dds = (DaoDataSource) ic.lookup(m.lookup());
                        return dds.getEntityManager();
                    }
                } else {
                    throw new MissingAnnotationException("Subclass of DaoDataSource.class does not have the @Manager annotation");
                }
            }

            throw new GenericDaoException("@Manager with name [" + managerName + "] not found");
        } catch (NamingException ex) {
            throw new GenericDaoException(ex);
        }
    }
}
