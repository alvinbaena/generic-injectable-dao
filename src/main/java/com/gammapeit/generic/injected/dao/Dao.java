/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gammapeit.generic.injected.dao;

import com.gammapeit.generic.injected.dao.jpa.HibernateJpaDao;
import java.io.Serializable;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Clase que identifica a una variable de instancia como un dao. Esta variable
 * debe ser de tipo {@link HibernateJpaDao} para que esta anotacion funcione
 * correctamente.
 *
 * @author bquest
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Dao {

    /**
     * El tipo de la entidad representada por el Dao.
     *
     * @return la clase que identifica el tipo de la entidad.
     */
    Class<? extends Serializable> entityClass();

    /**
     * (Opcional) el nombre del Session bean anotado con {@link Manager} y
     * subclase de {@link DaoDataSource}. Necesiario cuando se desea identificar
     * el datasource (EntityManager) que se va a usar. Si no se usa este
     * parametro se buscara el Manager por defecto.
     *
     * @see Manager#name()
     * @return El nombre del Manager.
     */
    String managerName() default "";
}
