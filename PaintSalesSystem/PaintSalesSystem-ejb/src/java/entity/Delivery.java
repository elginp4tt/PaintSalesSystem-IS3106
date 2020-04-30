/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author matto
 */
@Entity
public class Delivery implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryId;
    //Moved locationAddress, postalCode and deliveryTime from line item object to actual entity
    @Column(nullable = false)
    private String locationAddress;
    @Column(nullable = false)
    private String postalCode;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date deliveryStartTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date deliveryEndTime;
    //Edited to directly retrieve employee and deliveryServiceTransaction ids from entities
    //Removed setters for said fields
    @ManyToOne(optional = true)
    @JoinColumn(nullable = true)
    private Employee employee;
    @OneToOne(optional = false)
    @JoinColumn(nullable = false)
    private DeliveryServiceTransaction deliveryServiceTransaction;

    public Delivery() {
    }
    
    
    public Employee getEmployee() {
        return employee;
    }
    
    public void setEmployee(Employee employee) 
    {
        if(this.employee != null)
        {
            if(this.employee.getDeliveries().contains(this))
            {
                this.employee.getDeliveries().remove(this);
            }
        }
        this.employee = employee;
        
        if(this.employee != null)
        {
            if(!this.employee.getDeliveries().contains(this))
            {
                this.employee.getDeliveries().add(this);
            }
        }
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (deliveryId != null ? deliveryId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the deliveryId fields are not set
        if (!(object instanceof Delivery)) {
            return false;
        }
        Delivery other = (Delivery) object;
        if ((this.deliveryId == null && other.deliveryId != null) || (this.deliveryId != null && !this.deliveryId.equals(other.deliveryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Delivery[ id=" + deliveryId + " ]";
    }
    
    
    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
    }
    

    

    

    /**
     * @return the deliveryServiceTransaction
     */
    public DeliveryServiceTransaction getDeliveryServiceTransaction() {
        return deliveryServiceTransaction;
    }

    /**
     * @param deliveryServiceTransaction the deliveryServiceTransaction to set
     */
    public void setDeliveryServiceTransaction(DeliveryServiceTransaction deliveryServiceTransaction) {
        this.deliveryServiceTransaction = deliveryServiceTransaction;
    }

    /**
     * @return the locationAddress
     */
    public String getLocationAddress() {
        return locationAddress;
    }

    /**
     * @param locationAddress the locationAddress to set
     */
    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    

    public Date getDeliveryStartTime() {
        return deliveryStartTime;
    }

    public void setDeliveryStartTime(Date deliveryStartTime) {
        this.deliveryStartTime = deliveryStartTime;
    }

    public Date getDeliveryEndTime() {
        return deliveryEndTime;
    }

    public void setDeliveryEndTime(Date deliveryEndTime) {
        this.deliveryEndTime = deliveryEndTime;
    }
    
}
