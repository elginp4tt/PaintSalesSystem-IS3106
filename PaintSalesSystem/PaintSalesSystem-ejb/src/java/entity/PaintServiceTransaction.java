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
public class PaintServiceTransaction extends TransactionLineItem {

    
    @OneToOne(mappedBy = "paintServiceTransaction", cascade = CascadeType.PERSIST)
    private PaintService paintService;
   
    public PaintServiceTransaction() {
    }
    
    
    public PaintService getPaintService() 
    {
        return paintService;
    }
    
    public void setPaintService(PaintService paintService) 
    {
        this.paintService = paintService;
        if(paintService != null)
        {
            paintService.setPaintServiceTransaction(this);
        }
    }
    
}
