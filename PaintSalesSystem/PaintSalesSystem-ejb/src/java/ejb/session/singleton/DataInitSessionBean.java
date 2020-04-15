/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
import entity.Customer;
import entity.Employee;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author Elgin Patt
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean {

    @PersistenceContext(unitName = "PaintSalesSystem-ejbPU")
    private EntityManager em;

    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    public DataInitSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            employeeSessionBeanLocal.retrieveEmployeeById(1l);
        } catch (EmployeeNotFoundException ex) {
            initializeData();
        }
    }

    public void initializeData() {
        try {
            employeeSessionBeanLocal.createNewEmployee(new Employee("manager", "password", "Default", "manager", AccessRightEnum.MANAGER));
            customerEntitySessionBeanLocal.createNewCustomer(new Customer("Customer One", "One", "customerone@gmail.com", "Customer one home address", "customerone", "password"));
            customerEntitySessionBeanLocal.createNewCustomer(new Customer("Customer Two", "Two", "customertwo@gmail.com", "Customer two home address", "customertwo", "password"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
