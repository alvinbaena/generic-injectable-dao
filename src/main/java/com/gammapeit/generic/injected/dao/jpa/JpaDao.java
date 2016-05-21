/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gammapeit.generic.injected.dao.jpa;

import com.gammapeit.generic.injected.dao.InjectableEntity;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.atteo.classindex.IndexSubclasses;

/**
 * Contrato para un data access object que usa JPA.
 *
 * @author alvinbaena
 * @param <E> el tipo de la entidad.
 * @param <K> la llave primaria de la entidad.
 */
@IndexSubclasses
public abstract class JpaDao<E extends InjectableEntity, K> implements BaseDao<E, K> {

    protected EntityManager em;
    protected Class<E> entityClass;

    public JpaDao(Class<E> entityClass, EntityManager em) {
        this.entityClass = entityClass;
        this.em = em;
    }

    /**
     * Busca las entidades de una tabla usando un NamedQuery y parametros
     * opcionales.
     *
     * @param namedQuery el nombre del NamedQuery.
     * @param params un mapa con los parametros usados por el NamedQuery.
     * @return la lista con las entidades encontradas.
     */
    public abstract List<E> findWithNamedQuery(String namedQuery, Map<String, Object> params);

    /**
     * Busca una unica entidad usando un NamedQuery y parametros opcionales.
     *
     * @param namedQuery el nombre del NamedQuery.
     * @param params un mapa con los parametros usados por el NamedQuery.
     * @return La entidad encontrada, o null si no encuentra ninguna.
     */
    public abstract E findSingleWithNamedQuery(String namedQuery, Map<String, Object> params);

    /**
     * Cuenta las entidades de una tabla usando un NamedQuery con el formato
     * {@literal SELECT COUNT...} y parametros opcionales.
     *
     * @param named el nombre del NamedQuery.
     * @param params un mapa con los parametros usados por el NamedQuery.
     * @return el conteo devuelto por el NamedQuery.
     */
    public abstract Long countWithNamedQuery(String named, Map<String, Object> params);

    /**
     * Realiza una actualizacion (executeUpdate) con un named query.
     *
     * @param named el nombre del namedQuery
     * @param params un mapa con los parametros del query.
     */
    public abstract void executeWithNamedQuery(String named, Map<String, Object> params);

    /**
     * Busca las columnas obtenidas con una consulta usando operaciones
     * agregadas ({@literal MAX, SUM, AVG, MIN, DISTINCT, COUNT, GROUP BY)}, es
     * decir, consultas que no retornen toda la informacion de la entidad, sino
     * columnas especificas.
     *
     * @param <T> el tipo generico que del objeto que se quiere recibir.
     * @param namedQuery el nombre del NamedQuery
     * @param limit limita el numero de resultados a mostrar de la consulta.
     * Debe ser siempre mayor que 0, si no se cumple se retorna la lista
     * completa.
     * @param params un mapa con los parametros usados por el NamedQuery
     * @param type el tipo del objeto que se retorna. Si la consulta solamente
     * una columna el tipo debe ser el equivalente en java
     * {@literal (VARCHAR -> String, DECIMAL -> Double, etc)}, si se está
     * consultando mas de una sola columna se debe obligatoriamente tener el
     * tipo como {@literal Object[]}, si se esta consultando una entidad
     * completa (cuando se usa una consulta del tipo
     * {@literal SELECT * FROM...}) entonces el tipo ser la entidad que
     * representa la tabla que se consulta.
     * @return la lista con los resultados de la consulta, del tipo especificado
     * en el parametro.
     */
    public abstract <T> List<T> findAggregateWithNamedQuery(String namedQuery, int limit, Map<String, Object> params, Class<T> type);

    /**
     * Hace una consulta usando el lenguaje SQL (puro) de la base de datos usada
     * por el sistema (Oracle, MySQL, etc). Las consultas realizadas no son
     * independientes de la plataforma de base de datos.
     *
     * @param <T> el tipo generico del objeto que se retorna.
     * @param query la consulta en SQL nativo.
     * @param params los parametros de la consulta, en el orden especifico en el
     * que se encuentran en el SQL. Por ejemplo, si la consulta tiene tres
     * parametros ?1, ?2, y ?3 en arreglo de parametros debe tener un tamaño de
     * 3. En la primera posicion se encuentra el valor del parametro ?1, en la
     * segunda posicion se encuentra el valor del parametro ?2, y asi
     * sucesivamente.
     * @param type el tipo del objeto que se retorna. Si la consulta solamente
     * una columna el tipo debe ser el equivalente en java
     * {@literal (VARCHAR -> String, DECIMAL -> Double, etc)}, si se está
     * consultando mas de una sola columna se debe obligatoriamente tener el
     * tipo como {@literal Object[]}, si se esta consultando una entidad
     * completa (cuando se usa una consulta del tipo
     * {@literal SELECT * FROM...}) entonces el tipo ser la entidad que
     * representa la tabla que se consulta.
     * @return la lista con los resultados de la consulta, del tipo especificado
     * en el parametro.
     * @throws IllegalArgumentException si {@literal query} o {@literal type}
     * son nulos.
     */
    public abstract <T> List<T> doWithNativeQuery(String query, Object[] params, Class<T> type);

    /**
     * Realiza una actualizacion (executeUpdate) con un named query.
     *
     * @param query el query nativo de sql.
     * @param params los parametros de la sentencia, en el orden
     * especifico en el que se encuentran en el SQL. Por ejemplo, si la consulta
     * tiene tres parametros ?1, ?2, y ?3 en arreglo de parametros debe tener un
     * tamaño de 3. En la primera posicion se encuentra el valor del parametro
     * ?1, en la segunda posicion se encuentra el valor del parametro ?2, y asi
     * sucesivamente.
     */
    public abstract void executeWithNativeQuery(String query, Object[] params);

    /**
     * Busca en la base de datos una lista de entidades usando parametros de
     * ordenamiento y filtrado. La busqueda se hace paginando por base de datos,
     * es decir, se pueden obtener resultados en bloques.
     *
     * @param firstRow el indice del primer resultado a retornar.
     * @param numRows numero de resultados a retornar, empezando desde firstRow.
     * @param sort mapa de columnas (joins son validos, ej: columna.fk.col_fk)
     * con sus repsectivos ordenes (true = asc, false = desc).
     * @param filters mapa de los filtros a usar. El filtrado se hace agregando
     * los filtros a un conjunto de predicados unidos por sentencias AND (OR no
     * disponible)
     * @param hints mapa con las propiedades dependientes del provedor de
     * persistencia que se quieren usar en la consulta.
     * @return Una lista con las entidades filtradas y ordenadas que fueron
     * encontradas.
     */
    public abstract List<E> findLazy(int firstRow, int numRows, Map<String, Boolean> sort, Map<String, Object> filters, Map<String, String> hints);
}
