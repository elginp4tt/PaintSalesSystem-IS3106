/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import entity.Employee;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
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

    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    private Employee newEmployee;
    private List<Employee> employees;
    private Employee employeeToView;
    
    private List<Employee> filteredEmployees;
    
    
    public EmployeeManagementManagedBean() 
    {
        newEmployee = new Employee();
        employeeToView = new Employee();
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        setEmployees(employeeSessionBeanLocal.retrieveAllEmployee());
    }
    
    
    public void viewEmployeeDetail(ActionEvent event) throws IOException
    {
        Long employeeIdToView = (Long)event.getComponent().getAttributes().get("employeeId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("employeeIdToView", employeeIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewEmployeeDetails.xhtml");
    }
    
    
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
    

    
}
