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
public class MissingAnnotationException extends RuntimeException {

    public MissingAnnotationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingAnnotationException() {
    }

    public MissingAnnotationException(String message) {
        super(message);
    }

    public MissingAnnotationException(Throwable cause) {
        super(cause);
    }

}
