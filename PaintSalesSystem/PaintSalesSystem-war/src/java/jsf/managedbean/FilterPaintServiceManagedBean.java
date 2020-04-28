/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package jsf.managedbean;

import ejb.session.stateless.PaintServiceEntitySessionBeanLocal;
import entity.Delivery;
import entity.Employee;
import entity.PaintService;
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
@Named(value = "filterPaintServiceManagedBean")
@ViewScoped
public class FilterPaintServiceManagedBean implements Serializable
{
    
    
    
    @EJB
    private PaintServiceEntitySessionBeanLocal paintServiceEntitySessionBeanLocal;
    
    private String yearToFilter;
    private Date dateToFilter;
    private Date startDateFilter;
    private Date endDateFilter;
    
    
    private Boolean isYearFilterSelected;
    private Boolean isDateFilterSelected;
    private Boolean isIntervalFilterSelected;
    
    
    private Employee currentEmployee;
    private List<PaintService> filteredPaintServices;
    
    public FilterPaintServiceManagedBean()
    {
        isYearFilterSelected = false;
        isDateFilterSelected = false;
        isIntervalFilterSelected = false;
        filteredPaintServices = new ArrayList<>();
    }
    
    public void clean()
    {
        filteredPaintServices = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        setCurrentEmployee((Employee)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentEmployeeEntity"));
        List<PaintService> allPaintServices = paintServiceEntitySessionBeanLocal.retrieveAllPaintService();
        if(getCurrentEmployee().getAccessRightEnum().equals(AccessRightEnum.MANAGER))
        {
            filteredPaintServices = allPaintServices;
        }
        else
        {
            for(PaintService ps : allPaintServices)
            {
                if(ps.getEmployee().getEmployeeId().equals(getCurrentEmployee().getEmployeeId()))
                {
                    filteredPaintServices.add(ps);
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
                List<PaintService> allPaintServices = paintServiceEntitySessionBeanLocal.retrievePaintServiceByYear(yearToFilter);
                if(getCurrentEmployee().getAccessRightEnum().equals(AccessRightEnum.MANAGER))
                {
                    filteredPaintServices = allPaintServices;
                }
                else
                {
                    for(PaintService ps : allPaintServices)
                    {
                        if(ps.getEmployee().getEmployeeId().equals(getCurrentEmployee().getEmployeeId()))
                        {
                            filteredPaintServices.add(ps);
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
                List<PaintService> allPaintServices = paintServiceEntitySessionBeanLocal.retrievePaintServiceByDate(dateToFilter);
                if(getCurrentEmployee().getAccessRightEnum().equals(AccessRightEnum.MANAGER))
                {
                    filteredPaintServices = allPaintServices;
                }
                else
                {
                    for(PaintService ps : allPaintServices)
                    {
                        if(ps.getEmployee().getEmployeeId().equals(getCurrentEmployee().getEmployeeId()))
                        {
                            filteredPaintServices.add(ps);
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
                List<PaintService> allPaintServices = paintServiceEntitySessionBeanLocal.retrieveDeliveryByDates(startDateFilter, endDateFilter);
                if(getCurrentEmployee().getAccessRightEnum().equals(AccessRightEnum.MANAGER))
                {
                    filteredPaintServices = allPaintServices;
                }
                else
                {
                    for(PaintService ps : allPaintServices)
                    {
                        if(ps.getEmployee().getEmployeeId().equals(getCurrentEmployee().getEmployeeId()))
                        {
                            filteredPaintServices.add(ps);
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
    
    
    public PaintServiceEntitySessionBeanLocal getPaintServiceEntitySessionBeanLocal() {
        return paintServiceEntitySessionBeanLocal;
    }
    
    public void setPaintServiceEntitySessionBeanLocal(PaintServiceEntitySessionBeanLocal paintServiceEntitySessionBeanLocal) {
        this.paintServiceEntitySessionBeanLocal = paintServiceEntitySessionBeanLocal;
    }
    
    public String getYearToFilter() {
        return yearToFilter;
    }
    
    public void setYearToFilter(String yearToFilter) {
        this.yearToFilter = yearToFilter;
    }
    
    public Date getDateToFilter() {
        return dateToFilter;
    }
    
    public void setDateToFilter(Date dateToFilter) {
        this.dateToFilter = dateToFilter;
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
    
    public Boolean getIsYearFilterSelected() {
        return isYearFilterSelected;
    }
    
    public void setIsYearFilterSelected(Boolean isYearFilterSelected) {
        this.isYearFilterSelected = isYearFilterSelected;
    }
    
    public Boolean getIsDateFilterSelected() {
        return isDateFilterSelected;
    }
    
    public void setIsDateFilterSelected(Boolean isDateFilterSelected) {
        this.isDateFilterSelected = isDateFilterSelected;
    }
    
    public Boolean getIsIntervalFilterSelected() {
        return isIntervalFilterSelected;
    }
    
    public void setIsIntervalFilterSelected(Boolean isIntervalFilterSelected) {
        this.isIntervalFilterSelected = isIntervalFilterSelected;
    }
    
    public List<PaintService> getFilteredPaintServices() {
        return filteredPaintServices;
    }
    
    public void setFilteredPaintServices(List<PaintService> filteredPaintServices) {
        this.filteredPaintServices = filteredPaintServices;
    }
    
    public Employee getCurrentEmployee() {
        return currentEmployee;
    }
    
    public void setCurrentEmployee(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
    }
    
}
