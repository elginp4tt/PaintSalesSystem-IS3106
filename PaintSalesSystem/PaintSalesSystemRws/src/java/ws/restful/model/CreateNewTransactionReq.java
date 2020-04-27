/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Transaction;

/**
 *
 * @author Elgin Patt
 */
public class CreateNewTransactionReq {
    
    private Transaction newTransaction;
    private Long customerId;

    public CreateNewTransactionReq() {
    }

    public CreateNewTransactionReq(Transaction newTransaction, Long customerId) {
        this.newTransaction = newTransaction;
        this.customerId = customerId;
    }


    /**
     * @return the newTransaction
     */
    public Transaction getNewTransaction() {
        return newTransaction;
    }

    /**
     * @param newTransaction the newTransaction to set
     */
    public void setNewTransaction(Transaction newTransaction) {
        this.newTransaction = newTransaction;
    }

    /**
     * @return the customerId
     */
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId the customerId to set
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    
    
}
