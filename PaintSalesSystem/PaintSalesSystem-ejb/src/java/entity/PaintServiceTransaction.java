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
public class PaintServiceTransaction extends TransactionLineItem {

    
    @OneToOne(mappedBy = "paintServiceTransaction")
    private PaintService paintService;
   
    public PaintServiceTransaction() {
        super();
    }
    
    //Edited to take in associated paintservice entity only
    public PaintServiceTransaction(PaintService paintService) {
        this();
        this.paintService = paintService;
    }
    
    

//    public Long getPaintServiceTransactionId() {
//        return paintServiceTransactionId;
//    }
//
//    public void setPaintServiceTransactionId(Long paintServiceTransactionId) {
//        this.paintServiceTransactionId = paintServiceTransactionId;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (paintServiceTransactionId != null ? paintServiceTransactionId.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the paintServiceTransactionId fields are not set
//        if (!(object instanceof PaintServiceTransaction)) {
//            return false;
//        }
//        PaintServiceTransaction other = (PaintServiceTransaction) object;
//        if ((this.paintServiceTransactionId == null && other.paintServiceTransactionId != null) || (this.paintServiceTransactionId != null && !this.paintServiceTransactionId.equals(other.paintServiceTransactionId))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "entity.PaintServiceTransaction[ id=" + paintServiceTransactionId + " ]";
//    }

    /**
     * @return the paintService
     */
    public PaintService getPaintService() {
        return paintService;
    }

    /**
     * @param paintService the paintService to set
     */
    public void setPaintService(PaintService paintService) {
        this.paintService = paintService;
    }
    
}
