/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gammapeit.generic.injected.dao;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotacion que identifica una clase como Manager.
 *
 * @author alvinbaena
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Manager {

    /**
     * Nombre/identificador del manger, usado para diferenciar managers cuando
     * hay mas de un peristence unit.
     *
     * @return el nombre del manager.
     */
    String name();

    /**
     * String JNDI (preferiblemente portable) para buscar en el contenedor el
     * Session bean que se encuentra anotado con esta anotacion.
     *
     * @return el string JNDI.
     */
    String lookup();

    /**
     * Si el manager anotado es el por defecto. Si hay mas de una clase anotada
     * como default los resultados son impredecibles cuando se inyecta en entity
     * manager en GenericDaoFactory.
     *
     * @return si es un manager por defecto
     */
    boolean defaultManager() default false;
}
