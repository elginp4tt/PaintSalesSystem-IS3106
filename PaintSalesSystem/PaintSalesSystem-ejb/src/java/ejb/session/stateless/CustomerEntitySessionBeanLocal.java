/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import java.util.List;
import javax.ejb.Local;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteCustomerException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCustomerException;

/**
 *
 * @author CHEN BINGSEN
 */
@Local
public interface CustomerEntitySessionBeanLocal {

    public Long createNewCustomer(Customer newCustomer) throws UnknownPersistenceException, InputDataValidationException;

    public void deleteCustomer(Long customerId) throws CustomerNotFoundException, DeleteCustomerException;

    public void updateCustomer(Customer customer) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException;

    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException;

    public List<Customer> retrieveAllCustomers();

    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException;

    public Customer customerLogin(String username, String password) throws CustomerNotFoundException;

    public Customer updateCustomerForIonic(Customer customer) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException;

    public List<Customer> retrieveCustomerByCondition(String condition);

    public Customer makeCustomerMember(String username) throws CustomerNotFoundException;
    
}
