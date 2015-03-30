/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.bquest.generic.injected.dao;

/**
 *
 * @author bquest
 */
public class NotImplementedException extends RuntimeException {

    public NotImplementedException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotImplementedException() {
    }

    public NotImplementedException(String message) {
        super(message);
    }

    public NotImplementedException(Throwable cause) {
        super(cause);
    }

}
