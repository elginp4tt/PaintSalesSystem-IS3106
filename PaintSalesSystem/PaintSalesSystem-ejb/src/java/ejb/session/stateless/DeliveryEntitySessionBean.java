/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;


import entity.Delivery;
import entity.DeliveryServiceTransaction;
import entity.Employee;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.DeleteDeliveryException;
import util.exception.DeliveryNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateDeliveryException;

/**
 *
 * @author CHEN BINGSEN
 */
@Stateless
public class DeliveryEntitySessionBean implements DeliveryEntitySessionBeanLocal {


    @PersistenceContext(unitName = "PaintSalesSystem-ejbPU")
    private EntityManager em;

    
    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private final static DateTimeFormatter ft = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
    private final static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

    public DeliveryEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    
    @Override
    public Long createNewDelivery(Delivery newDelivery) throws UnknownPersistenceException,InputDataValidationException
    {
        try
        {
            Set<ConstraintViolation<Delivery>> constraintViolations = validator.validate(newDelivery);
            if(constraintViolations.isEmpty())
            {
                em.persist(newDelivery);
                em.flush();
                
                return newDelivery.getDeliveryId();
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        catch(PersistenceException ex)
        {
            throw new UnknownPersistenceException(ex.getMessage());
            
        }
        
    }
    
    
    
    @Override
    public List<Delivery> retrieveAllDelivery()
    {
        Query query = em.createQuery("SELECT d FROM Delivery d");
        return query.getResultList();
    }
    
    
    @Override
    public List<Delivery> retrieveDeliveryByYear(String year)
    {
        
        List<Delivery> result = new ArrayList<>();
        List<Delivery> allDeliveries = retrieveAllDelivery();
        
        int yearInt = Integer.parseInt(year);
        LocalDateTime curr;
        
        for(Delivery delivery: allDeliveries)
        {
            curr = LocalDateTime.parse(dateFormat.format(delivery.getDeliveryStartTime()),ft);
            if(curr.getYear() == yearInt)
            {
                result.add(delivery);
            }
        }
        
        return result;
        
        
    }
    
    
    
    @Override
    public List<Delivery> retrieveDeliveryByDate(Date date)
    {
        List<Delivery> result = new ArrayList<>();
        List<Delivery> allDeliveries = retrieveAllDelivery();
        
        LocalDateTime curr;
        LocalDateTime dateCompare = LocalDateTime.parse(dateFormat.format(date),ft);
        
        for(Delivery delivery: allDeliveries)
        {
            curr = LocalDateTime.parse(dateFormat.format(delivery.getDeliveryStartTime()),ft);
            if(curr.toLocalDate().isEqual(dateCompare.toLocalDate()))
            {
                result.add(delivery);
            }
        }
        
        return result;
    }
    
    
    
    @Override
    public List<Delivery> retrieveDeliveryByDates(Date startDate, Date endDate)
    {
        List<Delivery> result = new ArrayList<>();
        List<Delivery> allDeDdeliveries = retrieveAllDelivery();
        
        LocalDateTime curr;
        LocalDateTime startDateCompare = LocalDateTime.parse(dateFormat.format(startDate),ft);
        LocalDateTime endDateCompare = LocalDateTime.parse(dateFormat.format(endDate),ft);
        
        for(Delivery delivery: allDeDdeliveries)
        {
            curr = LocalDateTime.parse(dateFormat.format(delivery.getDeliveryStartTime()),ft);
            if(curr.toLocalDate().isEqual(startDateCompare.toLocalDate())
                    || curr.toLocalDate().isEqual(endDateCompare.toLocalDate())
                    || (curr.toLocalDate().isAfter(startDateCompare.toLocalDate()) && curr.toLocalDate().isBefore(endDateCompare.toLocalDate())))
            {
                result.add(delivery);
            }
        }
        
        return result;
    }
    
        
    
    
    
    @Override
    public Delivery retrieveDeliveryByDeliveryId(Long deliveryId) throws DeliveryNotFoundException
    {
        Delivery delivery = em.find(Delivery.class,deliveryId);
        
        if(delivery != null)
        {
            return delivery;
        }
        else
        {
            throw new DeliveryNotFoundException("Delivery ID " + deliveryId + " does not exists!");
        }
    }
    
    
    
    @Override
    public void checkAssignedEmployeeAvailability(Date newDeliveryStart, Date newDeliveryEnd, Long deliveryId, Long assignedEmployeeId) throws UpdateDeliveryException, DeliveryNotFoundException
    {
        if(deliveryId != null)
        {
            
            Delivery deliveryToUpdate = retrieveDeliveryByDeliveryId(deliveryId);
            
            List<Employee> availableEmployees = employeeSessionBeanLocal.retrieveAvailableEmployee(newDeliveryEnd, newDeliveryEnd, deliveryToUpdate.getDeliveryId(), null);
            
            boolean isFound = false;
            for(Employee employee : availableEmployees)
            {
                if(employee.getEmployeeId().equals(assignedEmployeeId))
                {
                    isFound = true;
                    break;
                }
            }
            
            if(!isFound)
            {
                throw new UpdateDeliveryException("The assigned employee is not available for this interval.");
            }
            
        }
    }
    
    
    
    @Override
    public void updateDelivery(Delivery delivery, Long employeeId) throws UpdateDeliveryException, DeliveryNotFoundException, EmployeeNotFoundException, InputDataValidationException
    {
        if(delivery != null && delivery.getDeliveryId()!= null)
        {
            Set<ConstraintViolation<Delivery>> constraintViolations = validator.validate(delivery);
            
            if(constraintViolations.isEmpty())
            {
                Delivery deliveryToUpdate = retrieveDeliveryByDeliveryId(delivery.getDeliveryId());
                Employee employeeToUpdate = employeeSessionBeanLocal.retrieveEmployeeById(employeeId);

                
                deliveryToUpdate.setEmployee(employeeToUpdate);
                deliveryToUpdate.setLocationAddress(delivery.getLocationAddress());
                deliveryToUpdate.setPostalCode(delivery.getPostalCode());
                deliveryToUpdate.setDeliveryStartTime(delivery.getDeliveryStartTime());
                deliveryToUpdate.setDeliveryEndTime(delivery.getDeliveryEndTime());
                
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new DeliveryNotFoundException("Delivery ID not provided for delivery record to be updated.");
        }
    }
    
    
    @Override
    public List<Employee> retrieveAvailableEmployeeByNewDeliveryDate(Delivery delivery, Date newDeliveryStartDate, Date newDeliveryEndDate) throws DeliveryNotFoundException
    {
        List<Employee> avaiEmployee = new ArrayList<>();
        
        if(delivery != null && delivery.getDeliveryId()!= null)
        {
            Delivery deliveryToUpdate = retrieveDeliveryByDeliveryId(delivery.getDeliveryId());
            
            avaiEmployee = employeeSessionBeanLocal.retrieveAvailableEmployee(newDeliveryStartDate, newDeliveryEndDate, deliveryToUpdate.getDeliveryId(), null);
            
        }
        
        return avaiEmployee;
    }
    
    
    
    @Override
    public void deleteDelivery(Long deliveryId) throws DeliveryNotFoundException, DeleteDeliveryException
    {
        Delivery deliveryToRemove = retrieveDeliveryByDeliveryId(deliveryId);
        
        if(deliveryToRemove.getDeliveryServiceTransaction() == null)
        {
            em.remove(deliveryToRemove);
        }
        else
        {
            throw new DeleteDeliveryException("Delivery Id " + deliveryId + " is associated with a delivery service transaction and cannot be deleted.");
        }
    }
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Delivery>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    } 
}
