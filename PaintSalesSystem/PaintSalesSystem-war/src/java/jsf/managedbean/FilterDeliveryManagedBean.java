/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package jsf.managedbean;

import ejb.session.stateless.DeliveryEntitySessionBeanLocal;
import entity.Delivery;
import entity.Employee;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.enumeration.AccessRightEnum;

/**
 *
 * @author CHEN BINGSEN
 */
@Named
@ViewScoped
public class FilterDeliveryManagedBean implements Serializable
{

    

    @EJB
    private DeliveryEntitySessionBeanLocal deliveryEntitySessionBeanLocal;
    
    
    private String yearToFilter;
    private Date dateToFilter;
    private Date startDateFilter;
    private Date endDateFilter;
    
    
    private Boolean isYearFilterSelected;
    private Boolean isDateFilterSelected;
    private Boolean isIntervalFilterSelected;
    
    
    private Employee currentEmployee;
    private List<Delivery> filteredDeliveries;
    
    public FilterDeliveryManagedBean()
    {
        isYearFilterSelected = false;
        isDateFilterSelected = false;
        isIntervalFilterSelected = false;
        filteredDeliveries = new ArrayList<>();
    }
    
    public void clean()
    {
        filteredDeliveries = new ArrayList<>();
    }
    
    
    @PostConstruct
    public void postConstruct()
    {
        currentEmployee = (Employee)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentEmployeeEntity");
        List<Delivery> allDeliveries = deliveryEntitySessionBeanLocal.retrieveAllDelivery();
        if(currentEmployee.getAccessRightEnum().equals(AccessRightEnum.MANAGER))
        {
            filteredDeliveries = allDeliveries;
        }
        else
        {
            for(Delivery d : allDeliveries)
            {
                if(d.getEmployee().getEmployeeId().equals(getCurrentEmployee().getEmployeeId()))
                {
                    filteredDeliveries.add(d);
                }
            }
        }
    }
    
    
    public void updateYearSelection()
    {
        isYearFilterSelected = true;
        isDateFilterSelected = false;
        isIntervalFilterSelected = false;
    }
    
    public void updateDateSelection()
    {
        isYearFilterSelected = false;
        isDateFilterSelected = true;
        isIntervalFilterSelected = false;
    }
    
    public void updateIntervalSelection()
    {
        isYearFilterSelected = false;
        isDateFilterSelected = false;
        isIntervalFilterSelected = true;
    }
    
    
    
    public void filter(ActionEvent event)
    {
        clean();
        
        if(isYearFilterSelected)
        {
            if(yearToFilter.isEmpty())
            {
                addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Year cannot be empty", null));
            }
            else
            {
                List<Delivery> allDeliveries = deliveryEntitySessionBeanLocal.retrieveDeliveryByYear(yearToFilter);
                if(getCurrentEmployee().getAccessRightEnum().equals(AccessRightEnum.MANAGER))
                {
                    filteredDeliveries = allDeliveries;
                }
                else
                {
                    for(Delivery d : allDeliveries)
                    {
                        if(d.getEmployee().getEmployeeId().equals(getCurrentEmployee().getEmployeeId()))
                        {
                            filteredDeliveries.add(d);
                        }
                    }
                }
            }
        }
        else if(isDateFilterSelected)
        {
            if(dateToFilter == null)
            {
                addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date cannot be empty", null));
            }
            else
            {
                List<Delivery> allDeliveries = deliveryEntitySessionBeanLocal.retrieveDeliveryByDate(dateToFilter);
                if(getCurrentEmployee().getAccessRightEnum().equals(AccessRightEnum.MANAGER))
                {
                    filteredDeliveries = allDeliveries;
                }
                else
                {
                    for(Delivery d : allDeliveries)
                    {
                        if(d.getEmployee().getEmployeeId().equals(getCurrentEmployee().getEmployeeId()))
                        {
                            filteredDeliveries.add(d);
                        }
                    }
                }
            }
            
        }
        else if(isIntervalFilterSelected)
        {
            if(startDateFilter == null || endDateFilter == null)
            {
                addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Dates cannot be empty", null));
            }
            else if(startDateFilter.after(endDateFilter))
            {
                addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Start date cannot be after end date.", null));
            }
            else
            {
                List<Delivery> allDeliveries = deliveryEntitySessionBeanLocal.retrieveDeliveryByDates(startDateFilter, endDateFilter);
                if(getCurrentEmployee().getAccessRightEnum().equals(AccessRightEnum.MANAGER))
                {
                    filteredDeliveries = allDeliveries;
                }
                else
                {
                    for(Delivery d : allDeliveries)
                    {
                        if(d.getEmployee().getEmployeeId().equals(getCurrentEmployee().getEmployeeId()))
                        {
                            filteredDeliveries.add(d);
                        }
                    }
                }
            }
        }
    }
    
    public void addMessage(FacesMessage message)
    {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public List<Delivery> getFilteredDeliveries() {
        return filteredDeliveries;
    }
    
    public void setFilteredDeliveries(List<Delivery> filteredDeliveries) {
        this.filteredDeliveries = filteredDeliveries;
    }
    
    public String getYearToFilter() {
        return yearToFilter;
    }
    
    public void setYearToFilter(String yearToFilter) {
        this.yearToFilter = yearToFilter;
    }
    
    public Boolean getIsYearFilterSelected() {
        return isYearFilterSelected;
    }
    
    public Boolean getIsDateFilterSelected() {
        return isDateFilterSelected;
    }
    
    public Date getDateToFilter() {
        return dateToFilter;
    }
    
    public void setDateToFilter(Date dateToFilter) {
        this.dateToFilter = dateToFilter;
    }
    
    public Boolean getIsIntervalFilterSelected() {
        return isIntervalFilterSelected;
    }
    
    public Date getStartDateFilter() {
        return startDateFilter;
    }
    
    public void setStartDateFilter(Date startDateFilter) {
        this.startDateFilter = startDateFilter;
    }
    
    public Date getEndDateFilter() {
        return endDateFilter;
    }
    
    public void setEndDateFilter(Date endDateFilter) {
        this.endDateFilter = endDateFilter;
    }


    public Employee getCurrentEmployee() {
        return currentEmployee;
    }

    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }

    
    public DeliveryEntitySessionBeanLocal getDeliveryEntitySessionBeanLocal() {
        return deliveryEntitySessionBeanLocal;
    }

    public void setDeliveryEntitySessionBeanLocal(DeliveryEntitySessionBeanLocal deliveryEntitySessionBeanLocal) {
        this.deliveryEntitySessionBeanLocal = deliveryEntitySessionBeanLocal;
    }
}
