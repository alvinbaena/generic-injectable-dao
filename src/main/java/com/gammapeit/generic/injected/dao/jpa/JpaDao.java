/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gammapeit.generic.injected.dao.jpa;

import com.gammapeit.generic.injected.dao.GenericDao;
import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Clase que tiene los metodos base (genericos) de un GenericDao
 * {@link GenericDao}, y añade los metodos de base de datos de jpa
 * {@link BaseJpaDao}.
 *
 * @author bquest
 * @param <E> Tipo de la entidad
 * @param <K> Tipo de la primary key de la entidad.
 */
public class JpaDao<E extends Serializable, K> extends BaseJpaDao<E, K> {

    private static final Logger log = Logger.getLogger(JpaDao.class.getName());

    public JpaDao(Class<E> entityClass, EntityManager em) {
        super(entityClass, em);
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
    public void executeWithNamedQuery(String named, Map<String, Object> params) {
        Query q = em.createNamedQuery(named);
        
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entrySet : params.entrySet()) {
                q.setParameter(entrySet.getKey(), entrySet.getValue());
            }
        }
        
        q.executeUpdate();
    }

    @Override
    public E findSingleWithNamedQuery(String named, Map<String, Object> params) {
        TypedQuery<E> q = em.createNamedQuery(named, entityClass);

        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entrySet : params.entrySet()) {
                q.setParameter(entrySet.getKey(), entrySet.getValue());
            }
        }

        List<E> results = q.getResultList();
        return results.isEmpty() ? null : results.get(0);
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
    public <T> List<T> findAggregateWithNamedQuery(String namedQuery, int limit, Map<String, Object> params, Class<T> type) {
        if (namedQuery != null && !namedQuery.isEmpty() && type != null) {
            Query q = em.createNamedQuery(namedQuery, type);

            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, Object> entrySet : params.entrySet()) {
                    q.setParameter(entrySet.getKey(), entrySet.getValue());
                }
            }

            return limit > 0 ? q.setMaxResults(limit).getResultList() : q.getResultList();
        } else {
            throw new IllegalArgumentException("Query o tipo nulos");
        }
    }

    @Override
    public <T> List<T> doWithNativeQuery(String query, Object[] params, Class<T> type) {
        if (query != null && !query.isEmpty() && type != null) {
            Query q = em.createNativeQuery(query, type);

            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    q.setParameter(i + 1, params[i]);
                }
            }

            return q.getResultList();
        } else {
            throw new IllegalArgumentException("Query o tipo nulos");
        }
    }

    @Override
    public void executeWithNativeQuery(String query, Object[] params) {
        if (query != null && !query.isEmpty()) {
            Query q = em.createNativeQuery(query);

            if (params != null && params.length > 0) {
                for (int i = 0; i < params.length; i++) {
                    q.setParameter(i + 1, params[i]);
                }
            }

            q.executeUpdate();
        } else {
            throw new IllegalArgumentException("Query o tipo nulos");
        }
    }

    @Override
    public List<E> findLazy(int firstRow, int numRows, Map<String, Boolean> sort, Map<String, Object> filters, Map<String, String> hints) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<E> cq = cb.createQuery(entityClass);
        Root<E> root = cq.from(entityClass);

        if (sort != null && !sort.isEmpty()) {
            List<Order> orders = new ArrayList<Order>();
            for (Map.Entry<String, Boolean> sortField : sort.entrySet()) {
                if (sortField.getValue()) {
                    orders.add(cb.asc(root.get(sortField.getKey())));
                } else {
                    orders.add(cb.desc(root.get(sortField.getKey())));
                }
            }

            cq.orderBy(orders);
        }

        Predicate f = cb.conjunction();
        if (filters != null && !filters.isEmpty()) {
            for (Map.Entry<String, Object> filter : filters.entrySet()) {
                if (filter.getValue() instanceof String) {
                    if (!filter.getValue().equals("")) {
                        String valueString = (String) filter.getValue();
                        f = cb.and(f, cb.like(cb.upper(root.<String>get(filter.getKey())), "%" + valueString.toUpperCase() + "%"));
                    }
                } else {
                    //Seguramente no es string.
                    f = cb.and(f, cb.equal(root.get(filter.getKey()), filter.getValue()));
                }
            }
        }

        cq.where(f);
        TypedQuery<E> q = em.createQuery(cq);

        if (firstRow > -1 && numRows > -1) {
            q.setFirstResult(firstRow);
            q.setMaxResults(numRows);
        }

        if (hints != null && !hints.isEmpty()) {
            for (Map.Entry<String, String> hint : hints.entrySet()) {
                q.setHint(hint.getKey(), hint.getValue());
            }
        }

        return q.getResultList();
    }
}
