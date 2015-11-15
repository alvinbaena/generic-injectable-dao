/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gammapeit.generic.injected.dao.exception;

/**
 *
 * @author alvinbaena
 */
public class GenericDaoException extends RuntimeException {

    public GenericDaoException() {
    }

    public GenericDaoException(String message) {
        super(message);
    }

    public GenericDaoException(Throwable cause) {
        super(cause);
    }

    public GenericDaoException(String message, Throwable cause) {
        super(message, cause);
    }

}
