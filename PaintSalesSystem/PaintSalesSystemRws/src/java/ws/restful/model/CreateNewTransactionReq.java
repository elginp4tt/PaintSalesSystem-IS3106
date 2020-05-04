/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.DeliveryServiceTransaction;
import entity.PaintServiceTransaction;
import entity.PaintTransaction;
import entity.TransactionLineItem;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CHEN BINGSEN
 */
public class CreateNewTransactionReq {

    

    private List<PaintTransaction> paintTransactions; 
    private List<DeliveryServiceTransaction> deliveryServiceTransactions;
    private List<PaintServiceTransaction> paintServiceTransactions;
    

    public CreateNewTransactionReq() {
    }
    
    public CreateNewTransactionReq(List<PaintTransaction> paintTransactions, List<DeliveryServiceTransaction> deliveryServiceTransactions, List<PaintServiceTransaction> paintServiceTransactions) {
        
        if(paintTransactions == null)
        {
            this.paintTransactions = new ArrayList<>();
        }
        else
        {
            this.paintTransactions = paintTransactions;
        }
        
        if(deliveryServiceTransactions == null)
        {
            this.deliveryServiceTransactions = new ArrayList<>();
        }
        else
        {
            this.deliveryServiceTransactions = deliveryServiceTransactions;
        }
        
        if(paintServiceTransactions == null)
        {
            this.paintServiceTransactions = new ArrayList<>();
        }
        else
        {
            this.paintServiceTransactions = paintServiceTransactions;
        }
    }
    
    public List<PaintTransaction> getPaintTransactions() {
        return paintTransactions;
    }

    public void setPaintTransactions(List<PaintTransaction> paintTransactions) {
        this.paintTransactions = paintTransactions;
    }

    public List<DeliveryServiceTransaction> getDeliveryServiceTransactions() {
        return deliveryServiceTransactions;
    }

    public void setDeliveryServiceTransactions(List<DeliveryServiceTransaction> deliveryServiceTransactions) {
        this.deliveryServiceTransactions = deliveryServiceTransactions;
    }

    public List<PaintServiceTransaction> getPaintServiceTransactions() {
        return paintServiceTransactions;
    }

    public void setPaintServiceTransactions(List<PaintServiceTransaction> paintServiceTransactions) {
        this.paintServiceTransactions = paintServiceTransactions;
    }
}
