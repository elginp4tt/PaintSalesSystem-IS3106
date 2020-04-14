/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Transaction;
import entity.TransactionLineItem;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CreateNewTransactionException;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author user
 */
@Stateless
public class TransactionSessionBean implements TransactionSessionBeanLocal {

    @EJB(name = "CustomerEntitySessionBeanLocal")
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @PersistenceContext(unitName = "PaintSalesSystem-ejbPU")
    private EntityManager em;
    
    @Resource
    private EJBContext eJBContext;
    
    
    
    public Transaction createTransaction (){
        
        return new Transaction();
    }
    
    
    @Override
    public Transaction createNewTransaction(Transaction newTransaction, Long customerId) throws CustomerNotFoundException, CreateNewTransactionException
    {
        if(newTransaction != null)
        {
            try
            {
                Customer customer = customerEntitySessionBeanLocal.retrieveCustomerByCustomerId(customerId);
                newTransaction.setCustomer(customer);
                customer.getTransactions().add(newTransaction);
                
                em.persist(newTransaction);
                
                for(TransactionLineItem transactionLineItem: newTransaction.getTransactionLineItems())
                {
                    //debit quantity
                    em.persist(transactionLineItem);
                }
                
                em.flush();
                return newTransaction;
            }
            catch(Exception ex)
            {
                eJBContext.setRollbackOnly();
                throw new CreateNewTransactionException(ex.getMessage());
            }
        }
        else
        {
            throw new CreateNewTransactionException("Transaction information not provided");
        }
    }
    
}
