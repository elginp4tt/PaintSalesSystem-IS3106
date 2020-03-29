/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author matto
 */
public class PaintExistException extends Exception{

    public PaintExistException() {
    }

    public PaintExistException(String message) {
        super(message);
    }
    
}
