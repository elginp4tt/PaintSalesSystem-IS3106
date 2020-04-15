/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.PaintServiceEntitySessionBeanLocal;
import entity.Employee;
import entity.PaintService;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import util.exception.DeliveryNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.PaintServiceNotFoundException;
import util.exception.UpdateDeliveryException;
import util.exception.UpdatePaintServiceException;

/**
 *
 * @author CHEN BINGSEN
 */
@Named(value = "paintServiceManagementManagedBean")
@ViewScoped

public class PaintServiceManagementManagedBean implements Serializable
{

    

    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    @EJB(name = "PaintServiceEntitySessionBeanLocal")
    private PaintServiceEntitySessionBeanLocal paintServiceEntitySessionBeanLocal;

    @Inject
    private ViewPaintServiceManagedBean viewPaintServiceManagedBean;
    
    
    //general
    private String radioSelection;
    private List<Employee> employees;
    private ScheduleModel eventModel;
    
    private List<PaintService> allPaintServices;
    private List<PaintService> filteredPaintServices;
    
    //update
    private List<Employee> availableEmployee;
    private PaintService selectedPaintServiceToUpdate;
    private Long employeeIdUpdate;
    private Date startTimeUpdate;
    private Date endTimeUpdate;
    private String addressUpdate;
    private String postalCodeUpdate;
    
    
    public PaintServiceManagementManagedBean() 
    {
        radioSelection = "table";
        eventModel = new DefaultScheduleModel();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        allPaintServices = paintServiceEntitySessionBeanLocal.retrieveAllPaintService();
        employees = employeeSessionBeanLocal.retrieveAllEmployee();
    }
    
    
    public void doUpdate(ActionEvent event)
    {
        selectedPaintServiceToUpdate = (PaintService)event.getComponent().getAttributes().get("paintServiceToUpdate");
        
        employeeIdUpdate = selectedPaintServiceToUpdate.getEmployee().getEmployeeId();
        startTimeUpdate = selectedPaintServiceToUpdate.getPaintServiceStartTime();
        endTimeUpdate = selectedPaintServiceToUpdate.getPaintServiceEndTime();
        addressUpdate = selectedPaintServiceToUpdate.getLocationAddress();
        postalCodeUpdate = selectedPaintServiceToUpdate.getPostalCode();
    }
    
    public void updatePaintService(ActionEvent event) throws IOException
    {
        try
        {
            if(startTimeUpdate.equals(endTimeUpdate) || startTimeUpdate.after(endTimeUpdate))
            {
                addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delivery service start time should be before end time.", null));
            }
            else
            {
                
                paintServiceEntitySessionBeanLocal.checkAssignedEmployeeAvailability(startTimeUpdate, endTimeUpdate, selectedPaintServiceToUpdate.getPaintServiceId(), employeeIdUpdate);
                
                selectedPaintServiceToUpdate.setLocationAddress(addressUpdate);
                selectedPaintServiceToUpdate.setPostalCode(postalCodeUpdate);
                selectedPaintServiceToUpdate.setPaintServiceStartTime(startTimeUpdate);
                selectedPaintServiceToUpdate.setPaintServiceEndTime(endTimeUpdate);
                
                paintServiceEntitySessionBeanLocal.updatePaintService(selectedPaintServiceToUpdate, employeeIdUpdate);
                
                for(Employee employee: employees)
                {
                    if(employee.getEmployeeId().equals(employeeIdUpdate))
                    {
                        selectedPaintServiceToUpdate.setEmployee(employee);
                        break;
                    }
                }
                
                addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Paint service updated successfully.", null));                
            }
        }
        catch(PaintServiceNotFoundException | EmployeeNotFoundException | InputDataValidationException | UpdatePaintServiceException ex)
        {
            addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
        
    }
    
    
    public void updateAvailableEmployeeSelection(ActionEvent event) throws PaintServiceNotFoundException
    {
        if(startTimeUpdate.equals(endTimeUpdate) || startTimeUpdate.after(endTimeUpdate))
        {
            availableEmployee = null;
            addMessage(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delivery service start time should be before end time.", null));
        }
        else
        {
            availableEmployee = paintServiceEntitySessionBeanLocal.retrieveAvailableEmployeeByNewPaintServiceDate(selectedPaintServiceToUpdate, startTimeUpdate, endTimeUpdate);
        }
    }
    
    
    public void addMessage(FacesMessage message)
    {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public List<PaintService> getAllPaintServices() {
        return allPaintServices;
    }

    public void setAllPaintServices(List<PaintService> allPaintServices) {
        this.allPaintServices = allPaintServices;
    }

    public List<PaintService> getFilteredPaintServices() {
        return filteredPaintServices;
    }

    public void setFilteredPaintServices(List<PaintService> filteredPaintServices) {
        this.filteredPaintServices = filteredPaintServices;
    }

    public String getRadioSelection() {
        return radioSelection;
    }

    public void setRadioSelection(String radioSelection) {
        this.radioSelection = radioSelection;
    }
    
    public boolean checkRadioTable()
    {
        return this.radioSelection.equals("table");
    }
    
    
    public boolean checkRadioCalendar()
    {
        return this.radioSelection.equals("calendar");
    }

    public PaintService getSelectedPaintServiceToUpdate() {
        return selectedPaintServiceToUpdate;
    }

    public void setSelectedPaintServiceToUpdate(PaintService selectedPaintServiceToUpdate) {
        this.selectedPaintServiceToUpdate = selectedPaintServiceToUpdate;
    }

    public ViewPaintServiceManagedBean getViewPaintServiceManagedBean() {
        return viewPaintServiceManagedBean;
    }

    public void setViewPaintServiceManagedBean(ViewPaintServiceManagedBean viewPaintServiceManagedBean) {
        this.viewPaintServiceManagedBean = viewPaintServiceManagedBean;
    }

    public Long getEmployeeIdUpdate() {
        return employeeIdUpdate;
    }

    public void setEmployeeIdUpdate(Long employeeIdUpdate) {
        this.employeeIdUpdate = employeeIdUpdate;
    }

    public Date getStartTimeUpdate() {
        return startTimeUpdate;
    }

    public void setStartTimeUpdate(Date startTimeUpdate) {
        this.startTimeUpdate = startTimeUpdate;
    }

    public Date getEndTimeUpdate() {
        return endTimeUpdate;
    }

    public void setEndTimeUpdate(Date endTimeUpdate) {
        this.endTimeUpdate = endTimeUpdate;
    }

    public String getAddressUpdate() {
        return addressUpdate;
    }

    public void setAddressUpdate(String addressUpdate) {
        this.addressUpdate = addressUpdate;
    }

    public String getPostalCodeUpdate() {
        return postalCodeUpdate;
    }

    public void setPostalCodeUpdate(String postalCodeUpdate) {
        this.postalCodeUpdate = postalCodeUpdate;
    }
    
    public List<Employee> getAvailableEmployee() {
        return availableEmployee;
    }

    public void setAvailableEmployee(List<Employee> availableEmployee) {
        this.availableEmployee = availableEmployee;
    }
    
    public ScheduleModel getEventModel() {
        return eventModel;
    }
    
    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }
    
}
