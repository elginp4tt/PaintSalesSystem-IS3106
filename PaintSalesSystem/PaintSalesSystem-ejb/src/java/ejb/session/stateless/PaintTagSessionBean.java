/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PaintTag;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewTagException;
import util.exception.DeleteTagException;
import util.exception.InputDataValidationException;
import util.exception.TagNotFoundException;
import util.exception.UpdateTagException;

/**
 *
 * @author matto
 */
@Stateless
@Local(PaintTagSessionBeanLocal.class)

public class PaintTagSessionBean implements PaintTagSessionBeanLocal {

    @PersistenceContext(unitName = "PaintSalesSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public PaintTagSessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    
    @Override
    public PaintTag createNewPaintTag(PaintTag newPaintTag) throws InputDataValidationException, CreateNewTagException
    {
        Set<ConstraintViolation<PaintTag>>constraintViolations = validator.validate(newPaintTag);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                em.persist(newPaintTag);
                em.flush();

                return newPaintTag;
            }
            catch(PersistenceException ex)
            {                
                if(ex.getCause() != null && 
                        ex.getCause().getCause() != null &&
                        ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
                {
                    throw new CreateNewTagException("Tag with same name already exist");
                }
                else
                {
                    throw new CreateNewTagException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
            catch(Exception ex)
            {                
                throw new CreateNewTagException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    @Override
    public List<PaintTag> retrieveAllTags()
    {
        Query query = em.createQuery("SELECT t FROM PaintTag t ORDER BY t.name ASC");
        List<PaintTag> tagEntities = query.getResultList();
        
        for(PaintTag paintTag:tagEntities)
        {            
            paintTag.getPaints().size();
        }
        
        return tagEntities;
    }
    
    
    
    @Override
    public PaintTag retrieveTagByTagId(Long tagId) throws TagNotFoundException
    {
        PaintTag paintTag = em.find(PaintTag.class, tagId);
        
        if(paintTag != null)
        {
            return paintTag;
        }
        else
        {
            throw new TagNotFoundException("Tag ID " + tagId + " does not exist!");
        }               
    }
    
    
    
    @Override
    public void updateTag(PaintTag paintTag) throws InputDataValidationException, TagNotFoundException, UpdateTagException
    {
        Set<ConstraintViolation<PaintTag>>constraintViolations = validator.validate(paintTag);
        
        if(constraintViolations.isEmpty())
        {
            if(paintTag.getTagId()!= null)
            {
                PaintTag paintTagToUpdate = retrieveTagByTagId(paintTag.getTagId());
                
                Query query = em.createQuery("SELECT t FROM PaintTag t WHERE t.name = :inName AND t.tagId <> :inTagId");
                query.setParameter("inName", paintTag.getTagName());
                query.setParameter("inTagId", paintTag.getTagId());
                
                if(!query.getResultList().isEmpty())
                {
                    throw new UpdateTagException("The name of the tag to be updated is duplicated");
                }
                
                paintTagToUpdate.setTagName(paintTag.getTagName());                               
            }
            else
            {
                throw new TagNotFoundException("Tag ID not provided for tag to be updated");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    @Override
    public void deleteTag(Long tagId) throws TagNotFoundException, DeleteTagException
    {
        PaintTag paintTagToRemove = retrieveTagByTagId(tagId);
        
        if(!paintTagToRemove.getPaints().isEmpty())
        {
            throw new DeleteTagException("Tag ID " + tagId + " is associated with existing products and cannot be deleted!");
        }
        else
        {
            em.remove(paintTagToRemove);
        }                
    }
    
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<PaintTag>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
