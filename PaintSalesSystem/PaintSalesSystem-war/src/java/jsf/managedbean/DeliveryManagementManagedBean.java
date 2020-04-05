package jsf.managedbean;

import ejb.session.stateless.DeliveryEntitySessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import entity.Delivery;
import entity.Employee;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
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
@Named
@ViewScoped
public class DeliveryManagementManagedBean implements Serializable{

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    @EJB
    private DeliveryEntitySessionBeanLocal deliveryEntitySessionBeanLocal;

    private List<Delivery> allDeliveries;
    private List<Delivery> filteredDeliveries;
    private Delivery deliveryToView;
    
    //for update
    private Delivery selectedDeliveryToUpdate;
    private Long employeeIdUpdate;
    private List<Employee> availableEmployee;
    private Calendar startTimeUpdate;
    private Calendar endTimeUpdate;
    
    
    
    public DeliveryManagementManagedBean() 
    {
        deliveryToView = new Delivery();
        availableEmployee = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        setAllDeliveries(deliveryEntitySessionBeanLocal.retrieveAllDelivery());
    }
    
    
    public void doUpdateDelivery(ActionEvent event)
    {
        setSelectedDeliveryToUpdate((Delivery)event.getComponent().getAttributes().get("deliveryToUpdate"));
        setEmployeeIdUpdate(selectedDeliveryToUpdate.getEmployeeId());
        startTimeUpdate = null;
        endTimeUpdate = null;
        setAvailableEmployee(new ArrayList<>());
    }
    
    
    public void updateDelivery(ActionEvent event)
    {
        try
        {
            deliveryEntitySessionBeanLocal.updateDelivery(selectedDeliveryToUpdate, getEmployeeIdUpdate(), selectedDeliveryToUpdate.getDeliveryServiceTransactionId());
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Delivery updated successfully", null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }
    
    
    public void retrieveAvailableEmployee(ActionEvent event)
    {
        
        
        if(startTimeUpdate == null || endTimeUpdate == null)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delivery service start/end time cannot be empty.", null));
        }
        else if(startTimeUpdate.equals(endTimeUpdate) || startTimeUpdate.after(endTimeUpdate))
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delivery service start time should be before end time.", null));
        }
        else
        {
            setAvailableEmployee(employeeSessionBeanLocal.retrieveAvailableEmployeeByDate(startTimeUpdate.getTime(), endTimeUpdate.getTime()));
        }
    }

    public List<Delivery> getAllDeliveries() {
        return allDeliveries;
    }

    public void setAllDeliveries(List<Delivery> allDeliveries) {
        this.allDeliveries = allDeliveries;
    }

    public List<Delivery> getFilteredDeliveries() {
        return filteredDeliveries;
    }

    public void setFilteredDeliveries(List<Delivery> filteredDeliveries) {
        this.filteredDeliveries = filteredDeliveries;
    }

    public Delivery getSelectedDeliveryToUpdate() {
        return selectedDeliveryToUpdate;
    }

    public void setSelectedDeliveryToUpdate(Delivery selectedDeliveryToUpdate) {
        this.selectedDeliveryToUpdate = selectedDeliveryToUpdate;
    }

    public List<Employee> getAvailableEmployee() {
        return availableEmployee;
    }

    public void setAvailableEmployee(List<Employee> availableEmployee) {
        this.availableEmployee = availableEmployee;
    }
    
    public Long getEmployeeIdUpdate() {
        return employeeIdUpdate;
    }

    public void setEmployeeIdUpdate(Long employeeIdUpdate) {
        this.employeeIdUpdate = employeeIdUpdate;
    }
    
}
