/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.PaintServiceEntitySessionBeanLocal;
import entity.Delivery;
import entity.PaintService;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

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
    
    
    private List<PaintService> filteredPaintServices;
    
    public FilterPaintServiceManagedBean() 
    {
        isYearFilterSelected = false;
        isDateFilterSelected = false;
        isIntervalFilterSelected = false;
    }
    
    
    public void updateSelection(ActionEvent event)
    {
        String selection = (String)event.getComponent().getAttributes().get("selection");
        
        if(selection.equals("year"))
        {
            isYearFilterSelected = true;
            isDateFilterSelected = false;
            isIntervalFilterSelected = false;
        }
        else if(selection.equals("date"))
        {
            isYearFilterSelected = false;
            isDateFilterSelected = true;
            isIntervalFilterSelected = false;
        }else if(selection.equals("interval"))
        {
            isYearFilterSelected = false;
            isDateFilterSelected = false;
            isIntervalFilterSelected = true;
        }
            
    }
    
    
    public void filter(ActionEvent event)
    {
        if(isYearFilterSelected)
        {
            filteredPaintServices = paintServiceEntitySessionBeanLocal.retrievePaintServiceByYear(yearToFilter);
        }
        else if(isDateFilterSelected)
        {
            if(dateToFilter == null)
            {
                addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Date cannot be empty", null));
            }
            else
            {
                filteredPaintServices = paintServiceEntitySessionBeanLocal.retrievePaintServiceByDate(dateToFilter);
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
                filteredPaintServices = paintServiceEntitySessionBeanLocal.retrieveDeliveryByDates(startDateFilter, endDateFilter);
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
    
}
