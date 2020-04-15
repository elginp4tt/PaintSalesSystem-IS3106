/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
/**
 *
 * @author matto
 */
@Entity
public class DeliveryServiceTransaction extends TransactionLineItem {
    
    @OneToOne(mappedBy = "deliveryServiceTransaction")
    private Delivery delivery;

    public DeliveryServiceTransaction() 
    {
    }

    public Delivery getDelivery() {
        return delivery;
    }
    
    public void setDelivery(Delivery delivery)
    {
        this.delivery = delivery;
        delivery.setDeliveryServiceTransaction(this);
    }
    
    
    
    

    
    
    
}
