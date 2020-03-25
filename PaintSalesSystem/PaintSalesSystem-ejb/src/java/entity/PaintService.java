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
public class PaintService implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paintServiceId;

    //Moved locationAddress, postalCode from line item object to actual entity
    @Column(nullable = false)
    private String locationAddress;
    @Column(nullable = false)
    private String postalCode;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date paintServiceTime;
    
    //Edited to directly retrieve employee and deliveryServiceTransaction ids from entities
    //Removed setters for said fields
    
    @OneToOne
    private PaintServiceTransaction paintServiceTransaction;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = true)
    private Employee employee;

    public PaintService() {
    }

    public PaintService(Employee employee) {
        this();
        this.employee = employee;
    }
    
    public Long getPaintServiceId() {
        return paintServiceId;
    }

    public void setPaintServiceId(Long paintServiceId) {
        this.paintServiceId = paintServiceId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paintServiceId != null ? paintServiceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the paintServiceId fields are not set
        if (!(object instanceof PaintService)) {
            return false;
        }
        PaintService other = (PaintService) object;
        if ((this.paintServiceId == null && other.paintServiceId != null) || (this.paintServiceId != null && !this.paintServiceId.equals(other.paintServiceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.PaintService[ id=" + paintServiceId + " ]";
    }

    /**
     * @return the paintServiceTransactionId
     */
    public Long getPaintServiceTransactionId() {
        return this.paintServiceTransaction.getTransactionLineItemId();
    }

    /**
     * @return the employeeId
     */
    public Long getEmployeeId() {
        return employee.getEmployeeId();
    }

    /**
     * @return the paintServiceTransaction
     */
    public PaintServiceTransaction getPaintServiceTransaction() {
        return paintServiceTransaction;
    }

    /**
     * @param paintServiceTransaction the paintServiceTransaction to set
     */
    public void setPaintServiceTransaction(PaintServiceTransaction paintServiceTransaction) {
        this.paintServiceTransaction = paintServiceTransaction;
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

    public Date getPaintServiceTime() {
        return paintServiceTime;
    }

    public void setPaintServiceTime(Date paintServiceTime) {
        this.paintServiceTime = paintServiceTime;
    }
    
}
