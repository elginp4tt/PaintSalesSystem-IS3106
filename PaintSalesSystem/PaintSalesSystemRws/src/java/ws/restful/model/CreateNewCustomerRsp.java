/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

/**
 *
 * @author Elgin Patt
 */
public class CreateNewCustomerRsp {
    
    private Long newCustomerId;

    public CreateNewCustomerRsp() {
    }

    public CreateNewCustomerRsp(Long newCustomerId) {
        this.newCustomerId = newCustomerId;
    }

    /**
     * @return the newCustomerId
     */
    public Long getNewCustomerId() {
        return newCustomerId;
    }

    /**
     * @param newCustomerId the newCustomerId to set
     */
    public void setNewCustomerId(Long newCustomerId) {
        this.newCustomerId = newCustomerId;
    }
    
}
