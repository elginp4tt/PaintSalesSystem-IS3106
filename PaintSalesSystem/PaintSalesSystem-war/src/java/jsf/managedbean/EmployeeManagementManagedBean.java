/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.DeliveryEntitySessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.PaintServiceEntitySessionBeanLocal;
import entity.Delivery;
import entity.DeliveryServiceTransaction;
import entity.Employee;
import entity.PaintService;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import util.exception.DeleteEmployeeException;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author CHEN BINGSEN
 */
@Named(value = "employeeManagementManagedBean")
@ViewScoped
public class EmployeeManagementManagedBean implements Serializable
{

    @EJB
    private PaintServiceEntitySessionBeanLocal paintServiceEntitySessionBeanLocal;
    @EJB
    private DeliveryEntitySessionBeanLocal deliveryEntitySessionBeanLocal;
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    

    private Employee newEmployee;
    private List<Employee> employees;
    private Employee employeeToView;
    
    private List<Employee> filteredEmployees;
    
    //for update
    private List<Long> deliveryIdsUpdate;
    private Employee selectedEmployeeToUpdate;
    private List<Long> paintServiceIdsUpdate;
    private List<Delivery> deliveries;
    private List<PaintService> paintServices;
    
    
    //for delete
    private Employee selectedEmployeeToDelete;
    
    
    public EmployeeManagementManagedBean() 
    {
        newEmployee = new Employee();
        employeeToView = new Employee();
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        setEmployees(employeeSessionBeanLocal.retrieveAllEmployee());
        deliveries = deliveryEntitySessionBeanLocal.retrieveAllDelivery();
        paintServices = paintServiceEntitySessionBeanLocal.retrieveAllPaintService();
    }
    
    
//    public void viewEmployeeDetail(ActionEvent event) throws IOException
//    {
//        Long employeeIdToView = (Long)event.getComponent().getAttributes().get("employeeId");
//        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("employeeIdToView", employeeIdToView);
//        FacesContext.getCurrentInstance().getExternalContext().redirect("viewEmployeeDetails.xhtml");
//    }
    
    
    public void createNewEmployee(ActionEvent event) throws IOException
    {
        try
        {
            Employee employee = employeeSessionBeanLocal.createNewEmployee(getNewEmployee());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New employee account has been created.", null));
        }
        catch(InputDataValidationException | UnknownPersistenceException | EmployeeUsernameExistException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }
    
    
    public void doUpdateEmployee(ActionEvent event)
    {
        selectedEmployeeToUpdate = (Employee)event.getComponent().getAttributes().get("employeeToUpdate");
        
        deliveryIdsUpdate = new ArrayList<>();
        paintServiceIdsUpdate = new ArrayList<>();
        
        for(Delivery delivery: selectedEmployeeToUpdate.getDeliveries())
        {
            deliveryIdsUpdate.add(delivery.getDeliveryId());
        }
        
        for(PaintService paintService:selectedEmployeeToUpdate.getPaintServices())
        {
            paintServiceIdsUpdate.add(paintService.getPaintServiceId());
        }
    }
    
    
    
    public void updateEmployee(ActionEvent event)
    {
        try
        {
            employeeSessionBeanLocal.updateEmployee(selectedEmployeeToUpdate, deliveryIdsUpdate, paintServiceIdsUpdate);
            
            selectedEmployeeToUpdate.getDeliveries().clear();
            for(Delivery de : deliveries)
            {
                if(deliveryIdsUpdate.contains(de.getDeliveryId()))
                {
                    selectedEmployeeToUpdate.getDeliveries().add(de);
                }
            }
            
            selectedEmployeeToUpdate.getPaintServices().clear();
            for(PaintService ps : paintServices)
            {
                if(paintServiceIdsUpdate.contains(ps.getPaintServiceId()))
                {
                    selectedEmployeeToUpdate.getPaintServices().add(ps);
                }
            }
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Employee updated successfully!", null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null));
        }
    }

    
    public void deleteEmployee(ActionEvent event)
    {
        try
        {
            selectedEmployeeToDelete = (Employee)event.getComponent().getAttributes().get("employeeToDelete");
            employeeSessionBeanLocal.deleteEmployee(selectedEmployeeToDelete.getEmployeeId());
            
            employees.remove(selectedEmployeeToDelete);
            
            if(filteredEmployees != null)
            {
                filteredEmployees.remove(selectedEmployeeToDelete);
            }
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Employee deleted successfully", null));
        }
        catch(EmployeeNotFoundException | DeleteEmployeeException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"An error has occurred while deleting employee: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    
    public Employee getNewEmployee() {
        return newEmployee;
    }

    public void setNewEmployee(Employee newEmployee) {
        this.newEmployee = newEmployee;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Employee> getFilteredEmployees() {
        return filteredEmployees;
    }

    public void setFilteredEmployees(List<Employee> filteredEmployees) {
        this.filteredEmployees = filteredEmployees;
    }

    public Employee getEmployeeToView() {
        return employeeToView;
    }

    public void setEmployeeToView(Employee employeeToView) {
        this.employeeToView = employeeToView;
    }

    public Employee getSelectedEmployeeToUpdate() {
        return selectedEmployeeToUpdate;
    }

    public void setSelectedEmployeeToUpdate(Employee selectedEmployeeToUpdate) {
        this.selectedEmployeeToUpdate = selectedEmployeeToUpdate;
    }
    

    
}
