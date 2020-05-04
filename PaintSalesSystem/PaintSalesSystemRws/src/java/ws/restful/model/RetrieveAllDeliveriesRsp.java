/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Delivery;
import java.util.List;

/**
 *
 * @author CHEN BINGSEN
 */
public class RetrieveAllDeliveriesRsp 
{

    
    private List<Delivery> deliveries;

    public RetrieveAllDeliveriesRsp() {
    }

    public RetrieveAllDeliveriesRsp(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }
    
    
    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }
}
