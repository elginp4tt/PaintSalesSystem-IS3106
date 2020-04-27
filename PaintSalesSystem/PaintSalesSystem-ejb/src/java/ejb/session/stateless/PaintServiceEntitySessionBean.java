/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.PaintService;
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
import util.exception.DeletePaintServiceException;
import util.exception.EmployeeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.PaintServiceNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdatePaintServiceException;

/**
 *
 * @author CHEN BINGSEN
 */
@Stateless
public class PaintServiceEntitySessionBean implements PaintServiceEntitySessionBeanLocal {


    @PersistenceContext(unitName = "PaintSalesSystem-ejbPU")
    private EntityManager em;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    private final static DateTimeFormatter ft = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
    private final static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

    public PaintServiceEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    
    @Override
    public Long createNewPaintService(PaintService newPaintService) throws UnknownPersistenceException,InputDataValidationException
    {
        try
        {
            Set<ConstraintViolation<PaintService>> constraintViolations = validator.validate(newPaintService);
            if(constraintViolations.isEmpty())
            {
                em.persist(newPaintService);
                em.flush();
                
                return newPaintService.getPaintServiceId();
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
    public List<PaintService> retrieveAllPaintService()
    {
        Query query = em.createQuery("SELECT ps FROM PaintService ps");
        return query.getResultList();
    }
    
    
    @Override
    public List<PaintService> retrievePaintServiceByYear(String year)
    {
        
        List<PaintService> result = new ArrayList<>();
        List<PaintService> allPaintServices = retrieveAllPaintService();
        
        int yearInt = Integer.parseInt(year);
        LocalDateTime curr;
        
        for(PaintService ps: allPaintServices)
        {
            curr = LocalDateTime.parse(dateFormat.format(ps.getPaintServiceStartTime()),ft);
            if(curr.getYear() == yearInt)
            {
                result.add(ps);
            }
        }
        
        return result;
    }
    
    @Override
    public List<PaintService> retrievePaintServiceByDate(Date date)
    {
        List<PaintService> result = new ArrayList<>();
        List<PaintService> allPaintServices = retrieveAllPaintService();
        
        LocalDateTime curr;
        LocalDateTime dateCompare = LocalDateTime.parse(dateFormat.format(date),ft);
        
        for(PaintService ps: allPaintServices)
        {
            curr = LocalDateTime.parse(dateFormat.format(ps.getPaintServiceStartTime()),ft);
            if(curr.toLocalDate().isEqual(dateCompare.toLocalDate()))
            {
                result.add(ps);
            }
        }
        
        return result;
    }
    
    
    @Override
    public List<PaintService> retrieveDeliveryByDates(Date startDate, Date endDate)
    {
        List<PaintService> result = new ArrayList<>();
        List<PaintService> allPaintServices = retrieveAllPaintService();
        
        LocalDateTime curr;
        LocalDateTime startDateCompare = LocalDateTime.parse(dateFormat.format(startDate),ft);
        LocalDateTime endDateCompare = LocalDateTime.parse(dateFormat.format(endDate),ft);
        
        for(PaintService ps: allPaintServices)
        {
            curr = LocalDateTime.parse(dateFormat.format(ps.getPaintServiceStartTime()),ft);
            if(curr.toLocalDate().isEqual(startDateCompare.toLocalDate())
                    || curr.toLocalDate().isEqual(endDateCompare.toLocalDate())
                    || (curr.toLocalDate().isAfter(startDateCompare.toLocalDate()) && curr.toLocalDate().isBefore(endDateCompare.toLocalDate())))
            {
                result.add(ps);
            }
        }
        
        return result;
    }
    
    
    
    @Override
    public List<Employee> retrieveAvailableEmployeeByNewPaintServiceDate(PaintService paintService, Date newStartDate, Date newEndDate) throws PaintServiceNotFoundException
    {
        List<Employee> avaiEmployee = new ArrayList<>();
        
        if(paintService != null && paintService.getPaintServiceId()!= null)
        {
            PaintService paintServiceToUpdate = retrievePaintServiceByPaintServiceId(paintService.getPaintServiceId());
            
            avaiEmployee = employeeSessionBeanLocal.retrieveAvailableEmployee(newStartDate, newEndDate, null, paintServiceToUpdate.getPaintServiceId());
            
        }
        
        return avaiEmployee;
    }
    
    
    @Override
    public void checkAssignedEmployeeAvailability(Date newStartTime, Date newEndTime, Long paintServiceId, Long assignedEmployeeId) throws UpdatePaintServiceException, PaintServiceNotFoundException
    {
        if(paintServiceId != null)
        {
            
            PaintService paintServiceToUpdate = retrievePaintServiceByPaintServiceId(paintServiceId);
            
            List<Employee> availableEmployees = employeeSessionBeanLocal.retrieveAvailableEmployee(newStartTime, newEndTime, null, paintServiceToUpdate.getPaintServiceId());
            
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
                throw new UpdatePaintServiceException("The assigned employee is not available for this interval.");
            }
            
        }
    }
    
    
    
    @Override
    public PaintService retrievePaintServiceByPaintServiceId(Long paintServiceId) throws PaintServiceNotFoundException
    {
        PaintService paintService = em.find(PaintService.class,paintServiceId);
        
        if(paintService != null)
        {
            return paintService;
        }
        else
        {
            throw new PaintServiceNotFoundException("Paint Service ID " + paintServiceId + " does not exists!");
        }
    }

    
    @Override
    public void updatePaintService(PaintService paintService, Long employeeId) throws PaintServiceNotFoundException, InputDataValidationException, EmployeeNotFoundException
    {
        if(paintService != null && paintService.getPaintServiceId()!= null)
        {
            Set<ConstraintViolation<PaintService>> constraintViolations = validator.validate(paintService);
            
            if(constraintViolations.isEmpty())
            {
                PaintService paintServiceToUpdate = retrievePaintServiceByPaintServiceId(paintService.getPaintServiceId());
                Employee employeeToUpdate = employeeSessionBeanLocal.retrieveEmployeeById(employeeId);
                
                
                paintServiceToUpdate.setEmployee(employeeToUpdate);
                paintServiceToUpdate.setLocationAddress(paintService.getLocationAddress());
                paintServiceToUpdate.setPostalCode(paintService.getPostalCode());
                paintServiceToUpdate.setPaintServiceStartTime(paintService.getPaintServiceStartTime());
                paintServiceToUpdate.setPaintServiceEndTime(paintService.getPaintServiceEndTime());
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new PaintServiceNotFoundException("Paint Service ID not provided for paint service record to be updated.");
        }
    }
    
    
    
    @Override
    public void deletePaintService(Long paintServiceId) throws PaintServiceNotFoundException, DeletePaintServiceException
    {
        PaintService paintServiceToRemove = retrievePaintServiceByPaintServiceId(paintServiceId);
        
        if(paintServiceToRemove.getPaintServiceTransaction() == null)
        {
            em.remove(paintServiceToRemove);
        }
        else
        {
            throw new DeletePaintServiceException("Paint Service Id " + paintServiceId + " is associated with a paint service transaction and cannot be deleted.");
        }
    }


    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<PaintService>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    } 
}
