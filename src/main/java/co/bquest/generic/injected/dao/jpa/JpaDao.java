/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.bquest.generic.injected.dao.jpa;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Contrato para un data access object que usa JPA.
 *
 * @author bquest
 * @param <E> el tipo de la entidad.
 * @param <K> la llave primaria de la entidad.
 */
public interface JpaDao<E extends Serializable, K> extends Dao<E, K> {

    /**
     * Busca las entidades de una tabla usando un NamedQuery y parametros
     * opcionales.
     *
     * @param namedQuery el nombre del NamedQuery.
     * @param params un mapa con los parametros usados por el NamedQuery.
     * @return la lista con las entidades encontradas.
     */
    List<E> findWithNamedQuery(String namedQuery, Map<String, Object> params);

    /**
     * Cuenta las entidades de una tabla usando un NamedQuery con el formato
     * {@literal SELECT COUNT...} y parametros opcionales.
     *
     * @param named el nombre del NamedQuery.
     * @param params un mapa con los parametros usados por el NamedQuery.
     * @return el conteo devuelto por el NamedQuery.
     */
    Long countWithNamedQuery(String named, Map<String, Object> params);

    /**
     * Busca las columnas obtenidas con una consulta usando operaciones
     * agregadas (MAX, SUM, AVG, MIN, DISTINCT, COUNT, GROUP BY), es decir,
     * consultas que no retornen toda la informacion de la entidad, sino
     * columnas especificas.
     *
     * @param namedQuery el nombre del NamedQuery
     * @param limit limita el numero de resultados a mostrar de la consulta.
     * Debe ser siempre mayor que 0, si no se cumple se retorna la lista
     * completa.
     * @param params un mapa con los parametros usados por el NamedQuery
     * @return una lista con los valores de las columnas retornadas por la
     * consulta.
     */
    List<Object> findAggregateWithNamedQuery(String namedQuery, int limit, Map<String, Object> params);

    /**
     * Hace una consulta usando el lenguaje SQL de la base de datos usada por el
     * sistema. No es independiente de la plataforma.
     *
     * @param <T> el tipo de objeto que se retorna.
     * @param query la consulta en SQL
     * @param params los parametros de la consulta, en el orden especifico en el
     * que se encuentran en el SQL.
     * @param type la clase del objeto que se retorna.
     * @return la lista con los resultados de la consulta.
     */
    <T> List<T> doWithNativeQuery(String query, Object[] params, Class<T> type);
}
