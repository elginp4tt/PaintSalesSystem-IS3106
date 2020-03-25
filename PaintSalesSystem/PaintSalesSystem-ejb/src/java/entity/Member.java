/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author matto
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Member extends Customer {
    
    private BigInteger loyaltyPoints;

    public Member() {
        super();
        loyaltyPoints = BigInteger.ZERO;
    }

    public Member(BigInteger loyaltyPoints) {
        this();
        this.loyaltyPoints = loyaltyPoints;
    }

    /**
     * @return the loyaltyPoints
     */
    public BigInteger getLoyaltyPoints() {
        return loyaltyPoints;
    }

    /**
     * @param loyaltyPoints the loyaltyPoints to set
     */
    public void setLoyaltyPoints(BigInteger loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }
    
}
