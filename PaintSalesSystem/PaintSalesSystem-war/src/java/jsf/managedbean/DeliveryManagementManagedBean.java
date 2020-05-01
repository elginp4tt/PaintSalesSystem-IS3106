package jsf.managedbean;

import ejb.session.stateless.DeliveryEntitySessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.MessageOfTheDayEntitySessionBeanLocal;
import entity.Delivery;
import entity.Employee;
import entity.MessageOfTheDay;
import entity.PaintService;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import util.enumeration.AccessRightEnum;
import util.exception.DeliveryNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UpdateDeliveryException;

/**
 *
 * @author CHEN BINGSEN
 */
@Named
@ViewScoped

public class DeliveryManagementManagedBean implements Serializable{

    @EJB(name = "MessageOfTheDayEntitySessionBeanLocal")
    private MessageOfTheDayEntitySessionBeanLocal messageOfTheDayEntitySessionBeanLocal;

    
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    
    @EJB
    private DeliveryEntitySessionBeanLocal deliveryEntitySessionBeanLocal;
    
    @Inject
    private ViewDeliveryManagedBean viewDeliveryManagedBean;
    
    private List<Employee> employees;
    private Employee currentEmployee;
    
    private List<Delivery> deliveriesToView;
    private List<Delivery> filteredDeliveries;
    
    
    
    //for update
    private List<Employee> availableEmployee;
    private Delivery selectedDeliveryToUpdate;
    private Long oldEmployeeId;
    private Long employeeIdUpdate;
    private Date startTimeUpdate;
    private Date endTimeUpdate;
    private String addressUpdate;
    private String postalCodeUpdate;
    
    
    public DeliveryManagementManagedBean()
    {
        deliveriesToView = new ArrayList<>();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        currentEmployee = (Employee)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentEmployeeEntity");
        List<Delivery> allDeliveries = deliveryEntitySessionBeanLocal.retrieveAllDelivery();
        if(currentEmployee.getAccessRightEnum().equals(AccessRightEnum.MANAGER))
        {
            deliveriesToView = allDeliveries;
        }
        else
        {
            for(Delivery d : allDeliveries)
            {
                if(d.getEmployee().getEmployeeId().equals(currentEmployee.getEmployeeId()))
                {
                    deliveriesToView.add(d);
                }
            }
        }
        employees = employeeSessionBeanLocal.retrieveAllEmployee();
    }
    
    
    
    public void doUpdate(ActionEvent event)
    {
        selectedDeliveryToUpdate = (Delivery)event.getComponent().getAttributes().get("deliveryToUpdate");
        
        if(selectedDeliveryToUpdate.getEmployee()!=null)
        {
            employeeIdUpdate = selectedDeliveryToUpdate.getEmployee().getEmployeeId();
            oldEmployeeId = employeeIdUpdate;
        }
        else
        {
            employeeIdUpdate = null;
            oldEmployeeId = null;
        }
        
        startTimeUpdate = selectedDeliveryToUpdate.getDeliveryStartTime();
        endTimeUpdate = selectedDeliveryToUpdate.getDeliveryEndTime();
        addressUpdate = selectedDeliveryToUpdate.getLocationAddress();
        postalCodeUpdate = selectedDeliveryToUpdate.getPostalCode();
    }
    
    public void updateDelivery(ActionEvent event) throws IOException
    {
        try
        {
            if(startTimeUpdate.equals(endTimeUpdate) || startTimeUpdate.after(endTimeUpdate))
            {
                FacesContext.getCurrentInstance().addMessage("startTimeUpdate", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delivery service start time should be before end time.", null));
            }
            else
            {
                
                deliveryEntitySessionBeanLocal.checkAssignedEmployeeAvailability(startTimeUpdate, endTimeUpdate, selectedDeliveryToUpdate.getDeliveryId(), employeeIdUpdate);
                
                
                selectedDeliveryToUpdate.setLocationAddress(addressUpdate);
                selectedDeliveryToUpdate.setPostalCode(postalCodeUpdate);
                selectedDeliveryToUpdate.setDeliveryStartTime(startTimeUpdate);
                selectedDeliveryToUpdate.setDeliveryEndTime(endTimeUpdate);
                
                deliveryEntitySessionBeanLocal.updateDelivery(selectedDeliveryToUpdate, employeeIdUpdate);
                
                if(employeeIdUpdate == null)
                {
                    selectedDeliveryToUpdate.setEmployee(null);
                }
                else
                {
                    for(Employee employee: employees)
                    {
                        if(employee.getEmployeeId().equals(employeeIdUpdate))
                        {
                            selectedDeliveryToUpdate.setEmployee(employee);
                            break;
                        }
                    }
                }
                
                
                
                if(oldEmployeeId!=null && employeeIdUpdate !=null && !oldEmployeeId.equals(employeeIdUpdate))
                {
                    MessageOfTheDay motd =  new MessageOfTheDay("Delivery Update", "An existing delivery has been assigned to other employee.", new Date());
                    messageOfTheDayEntitySessionBeanLocal.createNewMessageOfTheDay(motd);
                    Employee oldEmployee = employeeSessionBeanLocal.retrieveEmployeeById(oldEmployeeId);
                    oldEmployee.addMessageOfTheDay(motd);
                    employeeSessionBeanLocal.updateEmployeeMotd(oldEmployee);
                    motd =  new MessageOfTheDay("Delivery Update", "A new delivery has been assigned to you", new Date());
                    messageOfTheDayEntitySessionBeanLocal.createNewMessageOfTheDay(motd);
                    Employee newEmployee = employeeSessionBeanLocal.retrieveEmployeeById(employeeIdUpdate);
                    newEmployee.addMessageOfTheDay(motd);
                    employeeSessionBeanLocal.updateEmployeeMotd(newEmployee);
                }
                else if(oldEmployeeId!=null && employeeIdUpdate==null)
                {
                    MessageOfTheDay motd =  new MessageOfTheDay("Delivery Update", "An existing delivery has been cancelled.", new Date());
                    messageOfTheDayEntitySessionBeanLocal.createNewMessageOfTheDay(motd);
                    Employee oldEmployee = employeeSessionBeanLocal.retrieveEmployeeById(oldEmployeeId);
                    oldEmployee.addMessageOfTheDay(motd);
                    employeeSessionBeanLocal.updateEmployeeMotd(oldEmployee);
                }
                else if(oldEmployeeId==null && employeeIdUpdate!=null)
                {
                    MessageOfTheDay motd =  new MessageOfTheDay("Delivery Update", "A new delivery has been assigned to you", new Date());
                    messageOfTheDayEntitySessionBeanLocal.createNewMessageOfTheDay(motd);
                    Employee newEmployee = employeeSessionBeanLocal.retrieveEmployeeById(employeeIdUpdate);
                    newEmployee.addMessageOfTheDay(motd);
                    employeeSessionBeanLocal.updateEmployeeMotd(newEmployee);
                }
                
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Delivery updated successfully.", null));                
            }
        }
        catch(DeliveryNotFoundException | EmployeeNotFoundException | InputDataValidationException | UpdateDeliveryException ex)
        {
            FacesContext.getCurrentInstance().addMessage("growl",new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
        
    }
    
    
    public void updateAvailableEmployeeSelection(ActionEvent event) throws DeliveryNotFoundException
    {
        if(startTimeUpdate.equals(endTimeUpdate) || startTimeUpdate.after(endTimeUpdate))
        {
            availableEmployee = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Delivery service start time should be before end time.", null));
        }
        else
        {
            availableEmployee = deliveryEntitySessionBeanLocal.retrieveAvailableEmployeeByNewDeliveryDate(selectedDeliveryToUpdate, startTimeUpdate, endTimeUpdate);
        }
    }
    
    public Boolean displayUpdateButton()
    {
        if(currentEmployee.getAccessRightEnum().equals(AccessRightEnum.MANAGER))
        {
            return true;
        }
        else
        {
            return false;
        }
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
    
    public ViewDeliveryManagedBean getViewDeliveryManagedBean() {
        return viewDeliveryManagedBean;
    }
    
    public void setViewDeliveryManagedBean(ViewDeliveryManagedBean viewDeliveryManagedBean) {
        this.viewDeliveryManagedBean = viewDeliveryManagedBean;
    }
    
    
    
    public Long getEmployeeIdUpdate() {
        return employeeIdUpdate;
    }
    
    public void setEmployeeIdUpdate(Long employeeIdUpdate) {
        this.employeeIdUpdate = employeeIdUpdate;
    }

    public List<Employee> getAvailableEmployee() {
        return availableEmployee;
    }

    public void setAvailableEmployee(List<Employee> availableEmployee) {
        this.availableEmployee = availableEmployee;
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
    
    public List<Delivery> getDeliveriesToView() {
        return deliveriesToView;
    }

    public void setDeliveriesToView(List<Delivery> deliveriesToView) {
        this.deliveriesToView = deliveriesToView;
    }
    
}
