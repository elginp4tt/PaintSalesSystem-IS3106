/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author matto
 */
@Entity
public class PaintTransaction extends TransactionLineItem {

    @Column(nullable = false)
    private BigInteger volume;

    @OneToOne
    private Paint paint;
    
    public PaintTransaction() {
        super();
    }

    public PaintTransaction(BigInteger volume) {
        this();
        this.volume = volume;
    }

    /**
     * @return the volume
     */
    public BigInteger getVolume() {
        return volume;
    }

    /**
     * @param volume the volume to set
     */
    public void setVolume(BigInteger volume) {
        this.volume = volume;
    }

    /**
     * @return the paint
     */
    public Paint getPaint() {
        return paint;
    }

    /**
     * @param paint the paint to set
     */
    public void setPaint(Paint paint) {
        this.paint = paint;
    }
    
}
