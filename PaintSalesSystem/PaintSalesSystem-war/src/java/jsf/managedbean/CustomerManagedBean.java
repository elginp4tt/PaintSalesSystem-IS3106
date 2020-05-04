/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import entity.Customer;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteCustomerException;

/**
 *
 * @author Ko Jia Le
 */
@Named(value = "customerManagedBean")
@ViewScoped
public class CustomerManagedBean implements Serializable {

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    private List<Customer> customers;

    private List<Customer> filteredCustomer;

    private Customer customerToView;

    private Customer selectedCustomerToUpdate;

    private Customer selectedCustomerToDelete;

    private String condition;

    public CustomerManagedBean() {
        customerToView = new Customer();
        condition = "All Customer";
    }

    @PostConstruct
    public void postConstruct() {
        setCustomers(customerEntitySessionBeanLocal.retrieveAllCustomers());
    }

    public void filterCustomer() {
        customers = customerEntitySessionBeanLocal.retrieveCustomerByCondition(condition);
    }
    
    public boolean checkPoint (BigDecimal point){
        return point == null;
    }
    public void doUpdateCustomer(ActionEvent event) {
        selectedCustomerToUpdate = (Customer) event.getComponent().getAttributes().get("customerToUpdate");
    }

    public void updateCustomer(ActionEvent event) {
        try {
            customerEntitySessionBeanLocal.updateCustomer(selectedCustomerToUpdate);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Customer Updated Succesfully!", null));
        } catch (CustomerNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occured while updating the product: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occured: " + ex.getMessage(), null));
        }
    }

    public void deleteCustomer(ActionEvent event) {
        try {
            selectedCustomerToDelete = (Customer) event.getComponent().getAttributes().get("customerToDelete");
            customerEntitySessionBeanLocal.deleteCustomer(selectedCustomerToDelete.getCustomerId());
            customers.remove(selectedCustomerToDelete);

            if (filteredCustomer != null) {
                filteredCustomer.remove(selectedCustomerToDelete);
            }
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Customer deleted successfully", null));
        } catch (CustomerNotFoundException | DeleteCustomerException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occured while deleting customer: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occured: " + ex.getMessage(), null));
        }
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public Customer getCustomerToView() {
        return customerToView;
    }

    public void setCustomerToView(Customer customerToView) {
        this.customerToView = customerToView;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Customer getSelectedCustomerToUpdate() {
        return selectedCustomerToUpdate;
    }

    public void setSelectedCustomerToUpdate(Customer selectedCustomerToUpdate) {
        this.selectedCustomerToUpdate = selectedCustomerToUpdate;
    }

    public List<Customer> getFilteredCustomer() {
        return filteredCustomer;
    }

    public void setFilteredCustomer(List<Customer> filteredCustomer) {
        this.filteredCustomer = filteredCustomer;
    }

}
