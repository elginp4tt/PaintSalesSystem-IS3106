/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author matto
 */
@Entity
public class TransactionLineItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionLineItemId;
    
    private String itemName;
    private BigInteger quantity;
    private BigDecimal price;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Transaction transaction;
    
    public TransactionLineItem() {
    }

    public TransactionLineItem(String itemName, BigInteger quantity, BigDecimal price) {
        this();
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
    }
    
    public Long getTransactionLineItemId() {
        return transactionLineItemId;
    }

    public void setTransactionLineItemId(Long transactionLineItemId) {
        this.transactionLineItemId = transactionLineItemId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (transactionLineItemId != null ? transactionLineItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the transactionLineItemId fields are not set
        if (!(object instanceof TransactionLineItem)) {
            return false;
        }
        TransactionLineItem other = (TransactionLineItem) object;
        if ((this.transactionLineItemId == null && other.transactionLineItemId != null) || (this.transactionLineItemId != null && !this.transactionLineItemId.equals(other.transactionLineItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TransactionLineItem[ id=" + transactionLineItemId + " ]";
    }

    /**
     * @return the itemName
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the quantity
     */
    public BigInteger getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(BigInteger quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the transaction
     */
    public Transaction getTransaction() {
        return transaction;
    }

    /**
     * @param transaction the transaction to set
     */
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    
}
