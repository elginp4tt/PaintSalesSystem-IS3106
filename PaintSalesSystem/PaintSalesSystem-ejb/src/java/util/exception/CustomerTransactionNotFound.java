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
public class CustomerTransactionNotFound extends Exception {

    /**
     * Creates a new instance of <code>CustomerTransactionNotFound</code>
     * without detail message.
     */
    public CustomerTransactionNotFound() {
    }

    /**
     * Constructs an instance of <code>CustomerTransactionNotFound</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CustomerTransactionNotFound(String msg) {
        super(msg);
    }
}
