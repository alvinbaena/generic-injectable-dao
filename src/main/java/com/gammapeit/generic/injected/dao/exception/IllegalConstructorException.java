/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gammapeit.generic.injected.dao.exception;

/**
 *
 * @author bquest
 */
public class IllegalConstructorException extends RuntimeException {

    public IllegalConstructorException() {
    }

    public IllegalConstructorException(String message) {
        super(message);
    }

    public IllegalConstructorException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalConstructorException(Throwable cause) {
        super(cause);
    }

}
