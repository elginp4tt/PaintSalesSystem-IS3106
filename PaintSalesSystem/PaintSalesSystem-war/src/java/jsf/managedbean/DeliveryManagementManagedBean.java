package jsf.managedbean;

import ejb.session.stateless.DeliveryEntitySessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import entity.Delivery;
import entity.Employee;
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
    
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    
    @EJB
    private DeliveryEntitySessionBeanLocal deliveryEntitySessionBeanLocal;
    
    @Inject
    private ViewDeliveryManagedBean viewDeliveryManagedBean;
    
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    
    private List<Delivery> allDeliveries;
    private List<Delivery> filteredDeliveries;
    private String radioSelection;
    private ScheduleModel eventModel;
    private List<Employee> employees;
    
    
    //for update
    private List<Employee> availableEmployee;
    private Delivery selectedDeliveryToUpdate;
    private Long employeeIdUpdate;
    private Date startTimeUpdate;
    private Date endTimeUpdate;
    private String addressUpdate;
    private String postalCodeUpdate;
    
    
    public DeliveryManagementManagedBean()
    {
        radioSelection = "table";
        eventModel = eventModel = new DefaultScheduleModel();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        allDeliveries = deliveryEntitySessionBeanLocal.retrieveAllDelivery();
        employees = employeeSessionBeanLocal.retrieveAllEmployee();


//        eventModel.addEvent(new DefaultScheduleEvent("event 1", previousDay8Pm(),previousDay11Pm()));
    }
    
    
//    private Calendar today()
//    {
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(calendar.get(Calendar.YEAR),
//                        calendar.get(Calendar.MONTH),
//                        calendar.get(Calendar.DATE),0,0,0);
//        
//        return calendar;
//    }
//    
//    private Date previousDay8Pm()
//    {
//        Calendar t = (Calendar) today().clone();
//        t.set(Calendar.AM_PM, Calendar.PM);
//        t.set(Calendar.DATE,t.get(Calendar.DATE) -1);
//        t.set(Calendar.HOUR, 8);
//        
//        return t.getTime();
//    }
//    
//    
//    private Date previousDay11Pm()
//    {
//        Calendar t = (Calendar) today().clone();
//        t.set(Calendar.AM_PM, Calendar.PM);
//        t.set(Calendar.DATE,t.get(Calendar.DATE) -1);
//        t.set(Calendar.HOUR, 11);
//        
//        return t.getTime();
//    }
    
    
    
    
    
    
    
    
    
    
    public void doUpdate(ActionEvent event)
    {
        selectedDeliveryToUpdate = (Delivery)event.getComponent().getAttributes().get("deliveryToUpdate");
        
        employeeIdUpdate = selectedDeliveryToUpdate.getEmployee().getEmployeeId();
        startTimeUpdate = selectedDeliveryToUpdate.getDeliveryStartTime();
        endTimeUpdate = selectedDeliveryToUpdate.getDeliveryEndTime();
        addressUpdate = selectedDeliveryToUpdate.getLocationAddress();
        postalCodeUpdate = selectedDeliveryToUpdate.getPostalCode();
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
                
                for(Employee employee: employees)
                {
                    if(employee.getEmployeeId().equals(employeeIdUpdate))
                    {
                        selectedDeliveryToUpdate.setEmployee(employee);
                        break;
                    }
                }
                
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Delivery updated successfully.", null));                
            }
        }
        catch(DeliveryNotFoundException | EmployeeNotFoundException | InputDataValidationException | UpdateDeliveryException ex)
        {
            FacesContext.getCurrentInstance().addMessage("growl",new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
        
    }
    
    
    public boolean checkRadioTable()
    {
        return this.radioSelection.equals("table");
    }
    
    
    public boolean checkRadioCalendar()
    {
        return this.radioSelection.equals("calendar");
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
    
    public ViewDeliveryManagedBean getViewDeliveryManagedBean() {
        return viewDeliveryManagedBean;
    }
    
    public void setViewDeliveryManagedBean(ViewDeliveryManagedBean viewDeliveryManagedBean) {
        this.viewDeliveryManagedBean = viewDeliveryManagedBean;
    }
    
    public String getRadioSelection() {
        return radioSelection;
    }
    
    public void setRadioSelection(String radioSelection) {
        this.radioSelection = radioSelection;
    }
    
    
    
    public Long getEmployeeIdUpdate() {
        return employeeIdUpdate;
    }
    
    public void setEmployeeIdUpdate(Long employeeIdUpdate) {
        this.employeeIdUpdate = employeeIdUpdate;
    }
    
    public ScheduleModel getEventModel() {
        return eventModel;
    }
    
    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
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
    
    
    
}
