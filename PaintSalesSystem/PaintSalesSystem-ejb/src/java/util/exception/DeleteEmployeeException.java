/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author Ko Jia Le
 */
public class DeleteEmployeeException extends Exception {

    public DeleteEmployeeException() {
    }

    public DeleteEmployeeException(String msg) {
        super(msg);
    }
}
