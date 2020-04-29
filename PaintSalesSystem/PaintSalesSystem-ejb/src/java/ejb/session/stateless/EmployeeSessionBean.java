/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Customer;
import entity.Delivery;
import entity.Employee;
import entity.MessageOfTheDay;
import entity.PaintService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    
    @Override
    public Employee createNewEmployee(Employee newEmployee) throws InputDataValidationException, UnknownPersistenceException, EmployeeUsernameExistException 
    {
        try
        {
            Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(newEmployee);
            
            if (constraintViolations.isEmpty()) 
            {
                em.persist(newEmployee);
                em.flush();
                return newEmployee;
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new EmployeeUsernameExistException("Duplicate username has already existed.");
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            else
            {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }
    
    
    
    @Override
    public List<Employee> retrieveAllEmployee() 
    {
        Query query = em.createQuery("SELECT e FROM Employee e");

        return query.getResultList();
    }
    
    
    
    @Override
    public void updateEmployeeMotd(Employee employee) throws EmployeeNotFoundException
    {
        if (employee != null & employee.getEmployeeId() != null) {
            Employee employeeToUpdate = retrieveEmployeeById(employee.getEmployeeId());
            employeeToUpdate.getMotds().clear();
            employeeToUpdate.setMotds(employee.getMotds());
            
        }
    }
    
    
    @Override
    public void updateEmployee(Employee employee) throws EmployeeNotFoundException, UpdateEmployeeException, InputDataValidationException
    {
        if (employee != null & employee.getEmployeeId() != null) {
            Set<ConstraintViolation<Employee>> constraintViolations = validator.validate(employee);
            
            if (constraintViolations.isEmpty()) {
                Employee employeeToUpdate = retrieveEmployeeById(employee.getEmployeeId());
                
                if (employeeToUpdate.getUsername().equals(employee.getUsername()))
                {
                    employeeToUpdate.setFirstName(employee.getFirstName());
                    employeeToUpdate.setLastName(employee.getLastName());
                    employeeToUpdate.setAccessRightEnum(employee.getAccessRightEnum());
                }
                else
                {
                    throw new UpdateEmployeeException("Username of employee record to be updated does not match the existing record");
                }
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new EmployeeNotFoundException("Employee ID not provided for employee to be updated");
        }
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
    public List<Employee> retrieveAvailableEmployee(Date startTime, Date endTime, Long deliveryId, Long paintServicId)
    {
        List<Employee> result = new ArrayList<>();
        Boolean isAvailable;
        for (Employee employee : retrieveAllEmployee()) {
            isAvailable = true;
            for(PaintService ps: employee.getPaintServices())
            {
                if(!ps.getPaintServiceId().equals(paintServicId))
                {
                    if(checkOverlap(startTime,endTime,ps.getPaintServiceStartTime(),ps.getPaintServiceEndTime()))
                    {
                        isAvailable = false;
                        break;
                    }
                }
            }
            
            if(isAvailable)
            {
                for(Delivery delivery: employee.getDeliveries())
                {
                    if(!delivery.getDeliveryId().equals(deliveryId))
                    {
                        if(checkOverlap(startTime, endTime,delivery.getDeliveryStartTime(), delivery.getDeliveryEndTime()))
                        {
                            isAvailable = false;
                            break;
                        }
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
    //startA, endA are the date of new delivery
    @Override
    public boolean checkOverlap(Date startA, Date endA, Date startB, Date endB)
    {
        
        DateTimeFormatter ft = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        
        
        LocalDateTime startALocal = LocalDateTime.parse(dateFormat.format(startA),ft);
        LocalDateTime endALocal = LocalDateTime.parse(dateFormat.format(endA),ft);
        
        
        LocalDateTime oneHourBeforeStartB = LocalDateTime.parse(dateFormat.format(startB),ft).minusHours(1);
        LocalDateTime oneHourAfterEndB = LocalDateTime.parse(dateFormat.format(endB),ft).plusHours(1);
        
        
        if(startALocal.isBefore(oneHourBeforeStartB))
        {
            if(endALocal.isBefore(oneHourBeforeStartB) || endALocal.isEqual(oneHourBeforeStartB))
            {
                return false;
            }
            else if(endALocal.isAfter(oneHourBeforeStartB))
            {
                return true;
            }
        }
        else if(startALocal.isEqual(oneHourBeforeStartB))
        {
            return true;
        }
        else if(startALocal.isAfter(oneHourBeforeStartB) && startALocal.isBefore(oneHourAfterEndB))
        {
            return true;
        }
        else if(startALocal.isEqual(oneHourAfterEndB) || startALocal.isAfter(oneHourAfterEndB))
        {
            return false;
        }
        
        return false;
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
    
    //Do i neeed to hash password?
    @Override
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException {
        try {
            Employee employeeEntity = retrieveEmployeeByUsername(username);
            if (employeeEntity.getPassword().equals(password)) {
                employeeEntity.getPaintServices().size();
                employeeEntity.getDeliveries().size();
                employeeEntity.getMotds();
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
