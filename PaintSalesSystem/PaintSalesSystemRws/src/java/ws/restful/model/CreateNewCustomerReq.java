/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Customer;

/**
 *
 * @author Elgin Patt
 */
public class CreateNewCustomerReq {
    
    private Customer newCustomer;


    public CreateNewCustomerReq() {
    }

    public CreateNewCustomerReq(Customer newCustomer) {
        this.newCustomer = newCustomer;
    }

    /**
     * @return the newCustomer
     */
    public Customer getNewCustomer() {
        return newCustomer;
    }

    /**
     * @param newCustomer the newCustomer to set
     */
    public void setNewCustomer(Customer newCustomer) {
        this.newCustomer = newCustomer;
    }

    
    
}
