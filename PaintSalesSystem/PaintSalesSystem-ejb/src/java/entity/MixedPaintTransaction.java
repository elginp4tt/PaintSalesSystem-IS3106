/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.HashMap;
import javax.persistence.Entity;

/**
 *
 * @author matto
 */
@Entity
public class MixedPaintTransaction extends PaintTransaction {
    
    private HashMap<Long, Double> mixedPaints;

    public MixedPaintTransaction() {
    }

    public MixedPaintTransaction(HashMap<Long, Double> mixedPaints) {
        this();
        this.mixedPaints = mixedPaints;
    }
    
//    public Long getMixedPaintTransactionId() {
//        return mixedPaintTransactionId;
//    }
//
//    public void setMixedPaintTransactionId(Long mixedPaintTransactionId) {
//        this.mixedPaintTransactionId = mixedPaintTransactionId;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 0;
//        hash += (mixedPaintTransactionId != null ? mixedPaintTransactionId.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object object) {
//        // TODO: Warning - this method won't work in the case the mixedPaintTransactionId fields are not set
//        if (!(object instanceof MixedPaintTransaction)) {
//            return false;
//        }
//        MixedPaintTransaction other = (MixedPaintTransaction) object;
//        if ((this.mixedPaintTransactionId == null && other.mixedPaintTransactionId != null) || (this.mixedPaintTransactionId != null && !this.mixedPaintTransactionId.equals(other.mixedPaintTransactionId))) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "entity.MixedPaintTransaction[ id=" + mixedPaintTransactionId + " ]";
//    }

    /**
     * @return the mixedPaints
     */
    public HashMap<Long, Double> getMixedPaints() {
        return mixedPaints;
    }

    /**
     * @param mixedPaints the mixedPaints to set
     */
    public void setMixedPaints(HashMap<Long, Double> mixedPaints) {
        this.mixedPaints = mixedPaints;
    }
    
}
