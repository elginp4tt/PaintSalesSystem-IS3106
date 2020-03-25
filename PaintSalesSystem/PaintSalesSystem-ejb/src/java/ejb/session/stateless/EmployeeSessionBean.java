/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Employee;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.DeleteEmployeeException;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEmployeeException;

/**
 *
 * @author Ko Jia Le
 */
@Stateless
public class EmployeeSessionBean implements EmployeeSessionBeanLocal {

    @PersistenceContext(unitName = "PaintSalesSystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public EmployeeSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public Employee createNewEmployee(String firstName, String lastName, String username, String password) throws InputDataValidationException, UnknownPersistenceException, EmployeeUsernameExistException {
        Employee newEmployee = new Employee(firstName, lastName, username, password);

        try {
            Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(newEmployee);
            if (constraintViolations.isEmpty()) {
                em.persist(newEmployee);
                em.flush();
                return newEmployee;
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new EmployeeUsernameExistException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    public List<Employee> retrieveAllEmployee() {
        Query query = em.createQuery("SELECT e FROM Employee e");

        return query.getResultList();
    }

    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException {
        Employee employeeEntity = em.find(Employee.class, employeeId);

        if (employeeEntity != null) {
            return employeeEntity;
        } else {
            throw new EmployeeNotFoundException("Employee Id " + employeeId + " does not exist!");
        }
    }

    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException {
        Query query = em.createQuery("Select e FROM Employee e WHERE e.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (Employee) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new EmployeeNotFoundException("Employee Username " + username + " does not exist!");
        }
    }
    
    //Can be changed into update anything about employee later
    public Employee updateEmployeePassword(String username, String oldPassword, String newPassword) throws EmployeeNotFoundException, UpdateEmployeeException, InputDataValidationException {
        Employee employeeEntityInDB = retrieveEmployeeByUsername(username);
        Employee preupdateEmployee = new Employee(employeeEntityInDB.getFirstName(), employeeEntityInDB.getLastName(), employeeEntityInDB.getUsername(),employeeEntityInDB.getPassword());
        
        Set<ConstraintViolation<Employee>>constraintViolations = validator.validate(preupdateEmployee);
        if (constraintViolations.isEmpty()){
            if (username.equals(employeeEntityInDB.getUsername())){
                if (oldPassword.equals(employeeEntityInDB.getPassword())){
                    employeeEntityInDB.setPassword(newPassword);
                    return employeeEntityInDB;
                } else {
                    throw new UpdateEmployeeException("Old Password of Employee does not match the exisiting record");
                }
            } else {
                    throw new UpdateEmployeeException("Username of Employee does not match the exisiting record");
                
            } 
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        } 
    }

    //Do i neeed to hash password?
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            Employee employeeEntity = retrieveEmployeeByUsername(username);
            if (employeeEntity.getPassword().equals(password)) {
                employeeEntity.getPaintServices().size();
                employeeEntity.getDeliveries().size();
                return employeeEntity;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (EmployeeNotFoundException employeeNotFoundException) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException, DeleteEmployeeException {
        Employee employeeToRemove = retrieveEmployeeById(employeeId);
        if (employeeToRemove.getDeliveries().isEmpty() && employeeToRemove.getPaintServices().isEmpty()){
            em.remove(employeeToRemove);
        } else {
            throw new DeleteEmployeeException ("Employee Id " + employeeId + " is associated with exisiting deliveries or paint services transaction(s) and cannot be deleted!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Employee>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
