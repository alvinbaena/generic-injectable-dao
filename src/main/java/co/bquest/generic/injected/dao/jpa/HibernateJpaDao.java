/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.bquest.generic.injected.dao.jpa;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author bquest
 * @param <E> Tipo de la entidad
 * @param <K> Tipo de la primary key de la entidad.
 */
public class HibernateJpaDao<E extends Serializable, K> implements JpaDao<E, K> {

    private static final Logger log = Logger.getLogger(HibernateJpaDao.class.getName());

    protected EntityManager em;
    protected Class<E> entityClass;

    public HibernateJpaDao(Class<E> entityClass, EntityManager em) {
        this.entityClass = entityClass;
        this.em = em;
    }

    @Override
    public void create(E e) {
        log.log(Level.FINE, "Creando entidad de tipo {0}", entityClass.getSimpleName());
        em.persist(e);
        em.flush();
    }

    @Override
    public void update(E e) {
        log.log(Level.FINE, "Actualizando entidad de tipo {0}", entityClass.getSimpleName());
        em.merge(e);
        em.flush();
    }

    @Override
    public void delete(K id) {
        log.log(Level.FINE, "Eliminando entidad de tipo {0}", entityClass.getSimpleName());
        E delete = em.getReference(entityClass, id);

        em.remove(delete);
        em.flush();
    }

    @Override
    public E find(K id) {
        return em.find(entityClass, id);
    }

    @Override
    public void createBatch(Collection<E> e) {
        log.log(Level.FINE, "Creando [{0}] entidades de tipo {1}", new Object[]{e.size(), entityClass.getSimpleName()});

        int i = 0;
        for (E ent : e) {
            em.persist(ent);

            if (i % 20 == 0) {
                em.flush();
            }
        }
        em.flush();

        i++;
    }

    @Override
    public void updateBatch(Collection<E> e) {
        log.log(Level.FINE, "Actualizando [{0}] entidades de tipo {1}", new Object[]{e.size(), entityClass.getSimpleName()});

        int i = 0;
        for (E ent : e) {
            em.merge(ent);

            if (i % 20 == 0) {
                em.flush();
            }
        }
        em.flush();

        i++;
    }

    @Override
    public void deleteBatch(Collection<K> ids) {
        log.log(Level.FINE, "Eliminando [{0}] entidades de tipo {1}", new Object[]{ids.size(), entityClass.getSimpleName()});

        int i = 0;
        for (K id : ids) {
            E delete = em.getReference(entityClass, id);
            em.remove(delete);

            if (i % 20 == 0) {
                em.flush();
            }
        }
        em.flush();

        i++;
    }

    @Override
    public List<E> findAll() {
        CriteriaQuery<E> cq = em.getCriteriaBuilder().createQuery(entityClass);
        Root<E> root = cq.from(entityClass);
        return em.createQuery(cq.select(root)).getResultList();
    }

    @Override
    public Long countAll() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        cq.select(cb.count(cq.from(entityClass)));
        return em.createQuery(cq).getSingleResult();
    }

    @Override
    public List<E> findWithNamedQuery(String named, Map<String, Object> params) {
        TypedQuery<E> q = em.createNamedQuery(named, entityClass);

        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entrySet : params.entrySet()) {
                q.setParameter(entrySet.getKey(), entrySet.getValue());
            }
        }

        return q.getResultList();
    }

    @Override
    public Long countWithNamedQuery(String named, Map<String, Object> params) {
        TypedQuery<Long> q = em.createNamedQuery(named, Long.class);

        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entrySet : params.entrySet()) {
                q.setParameter(entrySet.getKey(), entrySet.getValue());
            }
        }

        return q.getSingleResult();
    }

    @Override
    public List<Object> findAggregateWithNamedQuery(String namedQuery, int limit, Map<String, Object> params) {
        Query q = em.createNamedQuery(namedQuery);

        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entrySet : params.entrySet()) {
                q.setParameter(entrySet.getKey(), entrySet.getValue());
            }
        }

        if (limit > 0) {
            return q.setMaxResults(limit).getResultList();
        } else {
            return q.getResultList();
        }
    }

    @Override
    public <T> List<T> doWithNativeQuery(String query, Object[] params, Class<T> type) {
        Query q = em.createNativeQuery(query, type);

        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                q.setParameter(i + 1, params[i]);
            }
        }

        return q.getResultList();
    }
}
