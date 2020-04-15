/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Transaction;
import entity.TransactionLineItem;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewTransactionException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerTransactionNotFound;

/**
 *
 * @author user
 */
@Local
public interface TransactionSessionBeanLocal {

    public Transaction createTransaction(Customer customer, List<TransactionLineItem> transactionLineItems) throws CreateNewTransactionException;

    public List<Transaction> retrieveAllTransactionByUser(Long custId) throws CustomerTransactionNotFound;

    public Transaction retrieveIndividualTransactionByUser(Long custId, Long transactionId) throws CustomerTransactionNotFound;

    public List<Transaction> retrieveAllTransactionByAdmin();

    public Transaction newcreateNewTransaction(Transaction newTransaction, Long customerId) throws CustomerNotFoundException, CreateNewTransactionException;

    
    
}
