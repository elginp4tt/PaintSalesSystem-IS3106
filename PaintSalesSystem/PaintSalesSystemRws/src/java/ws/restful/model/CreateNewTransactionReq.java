/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.TransactionLineItem;
import java.util.List;

/**
 *
 * @author Ko Jia Le
 */
public class CreateNewTransactionReq {
    private List<TransactionLineItem> newTransactionLineItems;
    private Long customerId;

    public CreateNewTransactionReq() {
    }

    public CreateNewTransactionReq(List<TransactionLineItem> newTransactionLineItems, Long customerId) {
        this.newTransactionLineItems = newTransactionLineItems;
        this.customerId = customerId;
    }

    public List<TransactionLineItem> getNewTransactionLineItems() {
        return newTransactionLineItems;
    }

    public void setNewTransactionLineItems(List<TransactionLineItem> newTransactionLineItems) {
        this.newTransactionLineItems = newTransactionLineItems;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
    
    
}
