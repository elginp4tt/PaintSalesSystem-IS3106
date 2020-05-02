/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.DeliveryServiceTransaction;
import entity.Transaction;
import entity.TransactionLineItem;
import java.util.List;

/**
 *
 * @author Elgin Patt
 */
public class RetrieveTransactionRsp {
    private List<TransactionLineItem> transactionLineItems;
  
    public RetrieveTransactionRsp() {
    }

    public RetrieveTransactionRsp(List<TransactionLineItem> transactionLineItems) {
        this.transactionLineItems = transactionLineItems;
    }

    public List<TransactionLineItem> getTransactionLineItems() {
        return transactionLineItems;
    }

    public void setTransactionLineItems(List<TransactionLineItem> transactionLineItems) {
        this.transactionLineItems = transactionLineItems;
    }
    
}
