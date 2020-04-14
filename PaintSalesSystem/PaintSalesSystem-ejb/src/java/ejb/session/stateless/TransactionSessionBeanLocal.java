/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Transaction;
import javax.ejb.Local;
import util.exception.CreateNewTransactionException;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author user
 */
@Local
public interface TransactionSessionBeanLocal {

    public Transaction createNewTransaction(Transaction newTransaction, Long customerId) throws CustomerNotFoundException, CreateNewTransactionException;
    
}
