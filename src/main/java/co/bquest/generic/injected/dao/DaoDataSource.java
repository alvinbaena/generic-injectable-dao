/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.bquest.generic.injected.dao;

import javax.persistence.EntityManager;
import org.atteo.classindex.IndexSubclasses;

/**
 *
 * @author bquest
 */
@IndexSubclasses
public interface DaoDataSource {

    EntityManager getEntityManager();
}
