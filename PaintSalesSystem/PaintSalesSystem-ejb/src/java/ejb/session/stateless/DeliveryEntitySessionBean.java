/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;


import entity.Delivery;
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
import util.exception.DeleteDeliveryException;
import util.exception.DeliveryNotFoundException;
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

    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

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
    public void updateDelivery(Delivery delivery) throws DeliveryNotFoundException, InputDataValidationException
    {
        if(delivery != null && delivery.getDeliveryId()!= null)
        {
            Set<ConstraintViolation<Delivery>> constraintViolations = validator.validate(delivery);
            
            if(constraintViolations.isEmpty())
            {
                Delivery deliveryToUpdate = retrieveDeliveryByDeliveryId(delivery.getDeliveryId());
                
                deliveryToUpdate.setLocationAddress(delivery.getLocationAddress());
                deliveryToUpdate.setPostalCode(delivery.getPostalCode());
                deliveryToUpdate.setDeliveryTime(delivery.getDeliveryTime());
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
