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
public class LoginRsp {
    
    private Customer customerEntity;

    public LoginRsp() {
    }

    public LoginRsp(Customer customerEntity) {
        this.customerEntity = customerEntity;
    }

    /**
     * @return the customerEntity
     */
    public Customer getCustomerEntity() {
        return customerEntity;
    }

    /**
     * @param customerEntity the customerEntity to set
     */
    public void setCustomerEntity(Customer customerEntity) {
        this.customerEntity = customerEntity;
    }
    
    
    
}
