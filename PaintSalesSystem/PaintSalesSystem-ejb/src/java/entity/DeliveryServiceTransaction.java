/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
/**
 *
 * @author matto
 */
@Entity
public class DeliveryServiceTransaction extends TransactionLineItem {
    
    @OneToOne
    private Delivery delivery;

    public DeliveryServiceTransaction() {
    }

    //Edited to take in associated delivery entity only
    public DeliveryServiceTransaction(Delivery delivery) {
        this();
        this.delivery = delivery;
    }   

//    @Override
//    public String toString() {
//        return "entity.DeliveryServiceTransaction[ id=" + deliveryServiceTransactionId + " ]";
//    }

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
