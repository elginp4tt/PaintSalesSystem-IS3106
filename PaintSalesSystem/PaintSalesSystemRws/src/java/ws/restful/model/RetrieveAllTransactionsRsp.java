/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Transaction;
import java.util.List;

/**
 *
 * @author Elgin Patt
 */
public class RetrieveAllTransactionsRsp {
    private List<Transaction> transactions;

    public RetrieveAllTransactionsRsp() {
    }

    public RetrieveAllTransactionsRsp(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * @return the transactions
     */
    public List<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * @param transactions the transactions to set
     */
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    
}
