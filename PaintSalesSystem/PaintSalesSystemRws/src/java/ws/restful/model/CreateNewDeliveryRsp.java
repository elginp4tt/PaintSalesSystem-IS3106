/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Delivery;
import entity.DeliveryServiceTransaction;

/**
 *
 * @author CHEN BINGSEN
 */
public class CreateNewDeliveryRsp 
{

    private DeliveryServiceTransaction deliveryServiceTransaction;
    private Delivery delivery;

    public CreateNewDeliveryRsp() {
    }

    public CreateNewDeliveryRsp(DeliveryServiceTransaction deliveryServiceTransaction, Delivery delivery) {
        this.deliveryServiceTransaction = deliveryServiceTransaction;
        this.delivery = delivery;
    }
    
    
    
    public DeliveryServiceTransaction getDeliveryServiceTransaction() {
        return deliveryServiceTransaction;
    }

    public void setDeliveryServiceTransaction(DeliveryServiceTransaction deliveryServiceTransaction) {
        this.deliveryServiceTransaction = deliveryServiceTransaction;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
    
}
