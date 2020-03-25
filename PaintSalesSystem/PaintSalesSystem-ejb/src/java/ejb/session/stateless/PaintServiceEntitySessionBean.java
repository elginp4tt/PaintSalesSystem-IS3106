/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Delivery;
import entity.PaintService;
import java.util.List;
import java.util.Set;
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
import util.exception.InputDataValidationException;
import util.exception.PaintServiceNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author CHEN BINGSEN
 */
@Stateless
public class PaintServiceEntitySessionBean implements PaintServiceEntitySessionBeanLocal {

    @PersistenceContext(unitName = "PaintSalesSystem-ejbPU")
    private EntityManager em;

    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

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
    public void updatePaintService(PaintService paintService) throws PaintServiceNotFoundException, InputDataValidationException
    {
        if(paintService != null && paintService.getPaintServiceId()!= null)
        {
            Set<ConstraintViolation<PaintService>> constraintViolations = validator.validate(paintService);
            
            if(constraintViolations.isEmpty())
            {
                PaintService paintServiceToUpdate = retrievePaintServiceByPaintServiceId(paintService.getPaintServiceId());
                
                paintServiceToUpdate.setLocationAddress(paintService.getLocationAddress());
                paintServiceToUpdate.setPostalCode(paintService.getPostalCode());
                paintServiceToUpdate.setPaintServiceTime(paintService.getPaintServiceTime());
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
