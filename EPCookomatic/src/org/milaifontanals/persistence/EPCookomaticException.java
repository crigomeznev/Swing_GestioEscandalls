/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.persistence;

/**
 *
 * @author Usuari
 */
public class EPCookomaticException extends RuntimeException {

    public EPCookomaticException(String message) {
        super(message);
    }

    public EPCookomaticException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
