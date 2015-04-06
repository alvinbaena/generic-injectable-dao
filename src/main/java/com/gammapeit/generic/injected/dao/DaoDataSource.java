/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gammapeit.generic.injected.dao;

import javax.persistence.EntityManager;
import org.atteo.classindex.IndexSubclasses;

/**
 * Interfaz que identifica un Session bean como dataSource para el GenericDao.
 *
 * @author bquest
 */
@IndexSubclasses
public interface DaoDataSource {

    /**
     * Da el dataSource especificado por el session bean.
     *
     * @return El entity manager.
     */
    EntityManager getEntityManager();
}
