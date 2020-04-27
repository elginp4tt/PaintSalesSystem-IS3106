/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Delivery;

/**
 *
 * @author Elgin Patt
 */
public class CreateNewDeliveryReq {
    private Delivery newDelivery;

    public CreateNewDeliveryReq() {
    }

    public CreateNewDeliveryReq(Delivery newDelivery) {
        this.newDelivery = newDelivery;
    }

    /**
     * @return the newDelivery
     */
    public Delivery getNewDelivery() {
        return newDelivery;
    }

    /**
     * @param newDelivery the newDelivery to set
     */
    public void setNewDelivery(Delivery newDelivery) {
        this.newDelivery = newDelivery;
    }
    
    
}
