/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Delivery;
import entity.Employee;
import entity.PaintService;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
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
import util.exception.DeliveryNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PaintServiceNotFoundException;
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

    @EJB
    private DeliveryEntitySessionBeanLocal deliveryEntitySessionBeanLocal;
    @EJB
    private PaintServiceEntitySessionBeanLocal paintServiceEntitySessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public EmployeeSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

//    @Override
//    public Employee createNewEmployee(String firstName, String lastName, String username, String password) throws InputDataValidationException, UnknownPersistenceException, EmployeeUsernameExistException {
//        Employee newEmployee = new Employee(firstName, lastName, username, password);
//    public Employee createNewEmployee(String firstName, String lastName, String username, String password) throws InputDataValidationException, UnknownPersistenceException, EmployeeUsernameExistException {
//        Employee newEmployee = new Employee(firstName, lastName, username, password);
//
//        try {
//            Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(newEmployee);
//            if (constraintViolations.isEmpty()) {
//                em.persist(newEmployee);
//                em.flush();
//                return newEmployee;
//            } else {
//                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
//            }
//        } catch (PersistenceException ex) {
//            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
//                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
//                    throw new EmployeeUsernameExistException();
//                } else {
//                    throw new UnknownPersistenceException(ex.getMessage());
//                }
//            } else {
//                throw new UnknownPersistenceException(ex.getMessage());
//            }
//        }
//    }
    @Override
    public Employee createNewEmployee(Employee newEmployee) throws InputDataValidationException, UnknownPersistenceException, EmployeeUsernameExistException {
        Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(newEmployee);

        if (constraintViolations.isEmpty()) {
            try {
                em.persist(newEmployee);
                em.flush();
                return newEmployee;
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
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }

    }

    @Override
    public List<Employee> retrieveAllEmployee() {
        Query query = em.createQuery("SELECT e FROM Employee e");

        return query.getResultList();
    }

    @Override
    public void updateEmployee(Employee employee, List<Long> deliveryIds, List<Long> paintServiceIds) throws EmployeeNotFoundException, DeliveryNotFoundException, PaintServiceNotFoundException, UpdateEmployeeException, InputDataValidationException {
        if (employee != null & employee.getEmployeeId() != null) {
            Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(employee);

            if (constraintViolations.isEmpty()) {
                Employee employeeToUpdate = retrieveEmployeeById(employee.getEmployeeId());

                if (employeeToUpdate.getUsername().equals(employee.getUsername())) {

                    if (deliveryIds != null) {
                        for (Delivery delivery : employeeToUpdate.getDeliveries()) {
                            delivery.setEmployee(null);
                        }
                        employeeToUpdate.getDeliveries().clear();

                        for (Long deliveryId : deliveryIds) {
                            Delivery delivery = deliveryEntitySessionBeanLocal.retrieveDeliveryByDeliveryId(deliveryId);
                            delivery.setEmployee(employeeToUpdate);
                        }
                    }

                    if (paintServiceIds != null) {
                        for (PaintService paintService : employeeToUpdate.getPaintServices()) {
                            paintService.setEmployee(null);
                        }
                        employeeToUpdate.getPaintServices().clear();

                        for (Long paintServiceId : paintServiceIds) {
                            PaintService paintService = paintServiceEntitySessionBeanLocal.retrievePaintServiceByPaintServiceId(paintServiceId);
                            paintService.setEmployee(employeeToUpdate);
                        }
                    }

                    employeeToUpdate.setFirstName(employee.getFirstName());
                    employeeToUpdate.setLastName(employee.getLastName());
                    employeeToUpdate.setAccessRightEnum(employee.getAccessRightEnum());
                    //username and password are not updated through this method
                } else {
                    throw new UpdateEmployeeException("Username of employee record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new EmployeeNotFoundException("Employee ID not provided for employee to be updated");
        }
    }

    @Override
    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException, DeleteEmployeeException {
        Employee employeeToRemove = retrieveEmployeeById(employeeId);

        if (employeeToRemove.getDeliveries().isEmpty() && employeeToRemove.getPaintServices().isEmpty()) {
            em.remove(employeeToRemove);
        } else {
            throw new DeleteEmployeeException("Employee Id " + employeeId + " is associated with exisiting deliveries or paint services transaction(s) and cannot be deleted!");
        }
    }

    @Override

    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException {
        Employee employeeEntity = em.find(Employee.class, employeeId);

        if (employeeEntity != null) {
            return employeeEntity;
        } else {
            throw new EmployeeNotFoundException("Employee Id " + employeeId + " does not exist!");
        }
    }

    @Override
    public List<Employee> retrieveAvailableEmployeeByDate(Date startTime, Date endTime) {

        List<Employee> result = new ArrayList<>();
        Boolean isAvailable;
        for (Employee employee : retrieveAllEmployee()) {
            isAvailable = true;
            for (PaintService ps : employee.getPaintServices()) {
                if (checkOverlap(ps.getPaintServiceStartTime(), ps.getPaintServiceEndTime(), startTime, endTime)) {
                    isAvailable = false;
                    break;
                }
            }

            if (isAvailable) {
                for (Delivery delivery : employee.getDeliveries()) {
                    if (checkOverlap(delivery.getDeliveryStartTime(), delivery.getDeliveryEndTime(), startTime, endTime)) {
                        isAvailable = false;
                        break;
                    }
                }
            }

            if (isAvailable) {
                result.add(employee);
            }
        }

        return result;
    }

    //return true if two intervals overlap, else return false
    public boolean checkOverlap(Date startA, Date endA, Date startB, Date endB) {
        Calendar calendarStartB = Calendar.getInstance();
        calendarStartB.setTime(startB);
        calendarStartB.add(Calendar.HOUR_OF_DAY, 1);

        Calendar calendarEndB = Calendar.getInstance();
        calendarEndB.setTime(startB);
        calendarEndB.add(Calendar.HOUR_OF_DAY, 1);

        if (startA.before(calendarStartB.getTime())) {
            if (endA.before(calendarStartB.getTime()) || endA.equals(calendarStartB.getTime())) {
                return false;
            } else {
                return true;
            }
        } else if (startA.equals(calendarStartB.getTime())) {
            return true;
        } else if (startA.after(calendarStartB.getTime()) && startA.before(calendarEndB.getTime())) {
            return true;
        } //        else if(startA.equals(calendarStartB.getTime()) || startA.after(calendarStartB.getTime()))
        //        {
        //            return false;
        //        }
        else {
            return false;
        }

    }

    @Override
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
    @Override
    public Employee updateEmployeePassword(String username, String oldPassword, String newPassword) throws EmployeeNotFoundException, UpdateEmployeeException, InputDataValidationException {
        Employee employeeEntityInDB = retrieveEmployeeByUsername(username);
        Employee preupdateEmployee = new Employee(employeeEntityInDB.getUsername(), newPassword, employeeEntityInDB.getFirstName(), employeeEntityInDB.getLastName(), employeeEntityInDB.getAccessRightEnum());

        Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(preupdateEmployee);
        if (constraintViolations.isEmpty()) {
            if (username.equals(employeeEntityInDB.getUsername())) {
                if (oldPassword.equals(employeeEntityInDB.getPassword())) {
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
//    public Employee updateEmployeePassword(String username, String oldPassword, String newPassword) throws EmployeeNotFoundException, UpdateEmployeeException, InputDataValidationException {
//        Employee employeeEntityInDB = retrieveEmployeeByUsername(username);
//        Employee preupdateEmployee = new Employee(employeeEntityInDB.getFirstName(), employeeEntityInDB.getLastName(), employeeEntityInDB.getUsername(),employeeEntityInDB.getPassword());
//        
//        Set<ConstraintViolation<Employee>>constraintViolations = validator.validate(preupdateEmployee);
//        if (constraintViolations.isEmpty()){
//            if (username.equals(employeeEntityInDB.getUsername())){
//                if (oldPassword.equals(employeeEntityInDB.getPassword())){
//                    employeeEntityInDB.setPassword(newPassword);
//                    return employeeEntityInDB;
//                } else {
//                    throw new UpdateEmployeeException("Old Password of Employee does not match the exisiting record");
//                }
//            } else {
//                    throw new UpdateEmployeeException("Username of Employee does not match the exisiting record");
//                
//            } 
//        } else {
//            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
//        } 
//    }

    //Do i neeed to hash password?
    @Override
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
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Employee>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
