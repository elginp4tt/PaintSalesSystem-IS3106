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
    private Date deliveryTime;
    
    //Edited to directly retrieve employee and deliveryServiceTransaction ids from entities
    //Removed setters for said fields
    @ManyToOne(optional = false)
    @JoinColumn(nullable = true)
    private Employee employee;
    @OneToOne(mappedBy = "delivery")
    private DeliveryServiceTransaction deliveryServiceTransaction;

    public Delivery() {
    }
    
    
    //Edited to only require employee upon initialization
    public Delivery(Employee employee) {
        this();
        this.employee = employee;
    }
    
    public Long getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(Long deliveryId) {
        this.deliveryId = deliveryId;
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
    
    
    
    /**
     * @return the deliveryServiceTransactionId
     */
    //Edited to get id from entity instead
    public Long getDeliveryServiceTransactionId() {
        return this.deliveryServiceTransaction.getTransactionLineItemId();
    }

    /**
     * @return the employeeId
     */
    //Edited to get id from entity instead
    public Long getEmployeeId() {
        return this.employee.getEmployeeId();
    }

    /**
     * @return the employee
     */
    public Employee getEmployee() {
        return employee;
    }

    /**
     * @param employee the employee to set
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
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

    /**
     * @return the deliveryTime
     */
    public Date getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * @param deliveryTime the deliveryTime to set
     */
    public void setDeliveryTime(Date deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
    
}
