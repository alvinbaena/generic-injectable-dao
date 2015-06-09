/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gammapeit.generic.injected.dao.jpa;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase de ayuda para la construccion de parametros para queries.
 *
 * <p>
 * La clase es un una envoltura soble un Map.
 * </p>
 *
 * @author bquest
 */
public class ParameterBuilder {

    private Map<String, Object> params;

    public ParameterBuilder() {
        this.params = new HashMap<String, Object>();
    }

    /**
     * Agrega un nuevo parametro. Si el parametro ya habia sido agregado antes
     * se sobreescribe el valor anterior.
     *
     * @param name el nombre del parametro.
     * @param value el valor del parametro.
     * @return la instancia de esta clase.
     */
    public ParameterBuilder setParameter(String name, Object value) {
        params.put(name, value);
        return this;
    }

    /**
     * Quita un parametro pre-existente. No hace nada si el parametro no
     * existia.
     *
     * @param name el nombre del parametro que se quiere eliminar.
     * @return la instancia de esta clase.
     */
    public ParameterBuilder removeParameter(String name) {
        params.remove(name);
        return this;
    }

    /**
     * Crea un Map con los parametros agregados previamente.
     *
     * @return el map con los parametros.
     */
    public Map<String, Object> build() {
        return params;
    }
}
