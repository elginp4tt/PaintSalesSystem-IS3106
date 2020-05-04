/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Customer;

/**
 *
 * @author Ko Jia Le
 */
public class UpdateCustomerReq {
    private Customer toUpdateCustomer;

    public UpdateCustomerReq() {
    }

    public UpdateCustomerReq(Customer toUpdateCustomer) {
        this.toUpdateCustomer = toUpdateCustomer;
    }

    public Customer getToUpdateCustomer() {
        return toUpdateCustomer;
    }

    public void setToUpdateCustomer(Customer toUpdateCustomer) {
        this.toUpdateCustomer = toUpdateCustomer;
    }
    
    
    
    
    
}
