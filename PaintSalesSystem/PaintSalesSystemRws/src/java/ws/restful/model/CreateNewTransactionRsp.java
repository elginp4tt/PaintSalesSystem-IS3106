/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Transaction;

/**
 *
 * @author CHEN BINGSEN
 */
public class CreateNewTransactionRsp 
{

    private Long transactionId;

    public CreateNewTransactionRsp() {
    }

    public CreateNewTransactionRsp(Long transactionId) {
        this.transactionId = transactionId;
    }
    
    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }
    
}
