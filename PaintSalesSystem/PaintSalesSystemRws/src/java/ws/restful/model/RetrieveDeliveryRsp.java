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
public class RetrieveDeliveryRsp {
    private Delivery delivery;

    public RetrieveDeliveryRsp() {
    }

    public RetrieveDeliveryRsp(Delivery delivery) {
        this.delivery = delivery;
    }

    /**
     * @return the delivery
     */
    public Delivery getDelivery() {
        return delivery;
    }

    /**
     * @param delivery the delivery to set
     */
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
    
    
}
