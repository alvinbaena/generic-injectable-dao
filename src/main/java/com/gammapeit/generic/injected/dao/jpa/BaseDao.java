/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gammapeit.generic.injected.dao.jpa;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * Contrato para un data access object.
 *
 * @author alvinbaena
 * @param <E> Tipo de entidad
 * @param <K> Tipo del primary key de la entidad.
 */
public interface BaseDao<E extends Serializable, K> {

    /**
     * Inserta un nuevo campo en la base de datos.
     *
     * @param e la entidad con la informaicion del campo a insertar.
     */
    void create(E e);

    /**
     * Actualiza un campo en la base de datos.
     *
     * @param e La entidad (obtenida previamente de la base de datos) con los
     * datos modificados.
     */
    void update(E e);

    /**
     * Elimina un campo en la base de datos.
     *
     * @param id El id del campo a eliminar.
     */
    void delete(K id);

    /**
     * Busca una entidad por su identificador.
     *
     * @param id el id de la entidad a buscar.
     * @return La entidad encontrada, o null si no se encuentra.
     */
    E find(K id);

    /**
     * Inserta varios campos en la base de datos, usando metodos optimizados
     * para batch.
     *
     * @param e la lista con las entidades a insertar.
     */
    void createBatch(Collection<E> e);

    /**
     * Actualiza varios campos en la base de datos, usando metodos optimizados
     * para batch.
     *
     * @param e la lista con las entidades a actualizar.
     */
    void updateBatch(Collection<E> e);

    /**
     * Elimina varios campos de la base de datos, usando metodos optimizados
     * para batch.
     *
     * @param ids una lista con los ids de los campos a eliminar.
     */
    void deleteBatch(Collection<K> ids);

    /**
     * Busca todas los campos de una tabla especifica.
     *
     * @return una lista con todas las entidades, las cuales representan todos
     * los campos en la tabla especifica de dicha entidad en la base de datos.
     */
    List<E> findAll();

    /**
     * Cuenta el numero de datos en una tabla.
     *
     * @return el numero de campos de la tabla.
     */
    Long countAll();
}
