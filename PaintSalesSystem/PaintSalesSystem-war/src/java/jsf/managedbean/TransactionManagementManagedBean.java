/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.TransactionSessionBeanLocal;
import entity.Transaction;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author CHEN BINGSEN
 */
@Named(value = "transactionManagementManagedBean")
@ViewScoped
public class TransactionManagementManagedBean implements Serializable {

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    private List<Transaction> transactions;

    private List<Transaction> filteredTransaction;

    private Transaction transactionToView;

    public TransactionManagementManagedBean() {
        transactionToView = new Transaction();
    }

    @PostConstruct
    public void postConstruct() {
        setTransactions(transactionSessionBeanLocal.retrieveAllTransactionByAdmin());
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Transaction> getFilteredTransaction() {
        return filteredTransaction;
    }

    public void setFilteredTransaction(List<Transaction> filteredTransaction) {
        this.filteredTransaction = filteredTransaction;
    }

    public Transaction getTransactionToView() {
        return transactionToView;
    }

    public void setTransactionToView(Transaction transactionToView) {
        this.transactionToView = transactionToView;
    }
}
