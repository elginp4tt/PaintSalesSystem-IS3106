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
public class transactionManagementManagedBean implements Serializable
{

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;

    private List<Transaction> transactions;
    private Transaction transactionToView;
    
    
    public transactionManagementManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct()
    {
        
    }
    
}
