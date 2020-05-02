/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Delivery;

/**
 *
 * @author CHEN BINGSEN
 */
public class UpdateDeliveryReq {

    
    
    
    //assume they can only update location, postal code, start time
    //assume that customer have the same timezone
    private Delivery delivery;

    public UpdateDeliveryReq() {
    }

    public UpdateDeliveryReq(Delivery delivery) {
        this.delivery = delivery;
    }
    
    
    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}
