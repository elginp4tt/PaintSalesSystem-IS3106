/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Paint;
import entity.PaintCategory;
import entity.PaintTag;
import entity.TransactionLineItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Local;
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
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewPaintException;
import util.exception.DeletePaintException;
import util.exception.InputDataValidationException;
import util.exception.PaintExistException;
import util.exception.PaintNotFoundException;
import util.exception.TagNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdatePaintException;

/**
 *
 * @author matto
 */
@Stateless
@Local(PaintSessionBeanLocal.class)
public class PaintSessionBean implements PaintSessionBeanLocal {

    @EJB
    private TransactionSessionBeanLocal transactionSessionBeanLocal;
    @EJB
    private PaintTagSessionBeanLocal paintTagSessionBeanLocal;
    @EJB
    private PaintCategorySessionBeanLocal paintCategorySessionBeanLocal;

    @PersistenceContext(unitName = "PaintSalesSystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public PaintSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
@Override
    public Paint createNewPaint(Paint newPaint, List<Long> categoryIds, List<Long> tagIds) throws PaintExistException, UnknownPersistenceException, InputDataValidationException, CreateNewPaintException
    {
        Set<ConstraintViolation<Paint>>constraintViolations = validator.validate(newPaint);
        
        if(constraintViolations.isEmpty())
        {  
            try
            {
                if(categoryIds == null || categoryIds.isEmpty())
                {
                    throw new CreateNewPaintException("The new paint must be associated a leaf category");
                }
                
                for(Long categoryId:categoryIds) {
                    PaintCategory paintCategory = paintCategorySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);
                
                    if(!paintCategory.getSubCategoryEntities().isEmpty())
                    {
                        throw new CreateNewPaintException("Selected category for the new paint is not a leaf category");
                    }
                    newPaint.getPaintCategories().add(paintCategory);
                }
                
                if(tagIds != null && (!tagIds.isEmpty()))
                {
                    for(Long tagId:tagIds)
                    {
                        PaintTag paintTag = paintTagSessionBeanLocal.retrieveTagByTagId(tagId);
                        newPaint.getTags().add(paintTag);
                    }
                }
                
                em.persist(newPaint);

                em.flush();

                return newPaint;
            }
            catch(PersistenceException ex)
            {
                if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
                {
                    if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                    {
                        throw new PaintExistException(ex.getMessage());
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
            catch(CategoryNotFoundException | TagNotFoundException ex)
            {
                throw new CreateNewPaintException("An error has occurred while creating the new paint: " + ex.getMessage());
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    @Override
    public List<Paint> retrieveAllPaints()
    {
        Query query = em.createQuery("SELECT p FROM Paint p ORDER BY p.colourCode ASC");        
        List<Paint> paints = query.getResultList();
        
        for(Paint paint:paints)
        {
            paint.getPaintCategories();
            paint.getTags().size();
        }
        
        return paints;
    }
    
    
    
    // Newly addded in v5.1
    
    @Override
    public List<Paint> searchPaintsByName(String searchString)
    {
        Query query = em.createQuery("SELECT p FROM Paint p WHERE p.name LIKE :inSearchString ORDER BY p.colourCode ASC");
        query.setParameter("inSearchString", "%" + searchString + "%");
        List<Paint> paints = query.getResultList();
        
        for(Paint paint:paints)
        {
            paint.getPaintCategories();
            paint.getTags().size();
        }
        
        return paints;
    }
        
    @Override
    public List<Paint> filterPaintsByCategories(List<Long> categoryIds, String condition)
    {
        List<Paint> paints = new ArrayList<>();
        
        if(categoryIds == null || categoryIds.isEmpty() || (!condition.equals("AND") && !condition.equals("OR")))
        {
            return paints;
        }
        else
        {
            if(condition.equals("OR"))
            {
                Query query = em.createQuery("SELECT DISTINCT p FROM Paint p, IN (p.paintCategories) pc WHERE pc.paintCategoryId IN :inCategoryIds ORDER BY p.colourCode ASC");
                query.setParameter("inCategoryIds", categoryIds);
                paints = query.getResultList();                                                          
            }
            else // AND
            {
                String selectClause = "SELECT p FROM Paint p";
                String whereClause = "";
                Boolean firstCategory = true;
                Integer categoryCount = 1;

                for(Long categoryId:categoryIds)
                {
                    selectClause += ", IN (p.paintCategories) pc" + categoryCount;

                    if(firstCategory)
                    {
                        whereClause = "WHERE pc1.paintCategoryId = " + categoryId;
                        firstCategory = false;
                    }
                    else
                    {
                        whereClause += " AND te" + categoryCount + ".tagId = " + categoryId; 
                    }
                    
                    categoryCount++;
                }
                
                String jpql = selectClause + " " + whereClause + " ORDER BY p.colourCode ASC";
                Query query = em.createQuery(jpql);
                paints = query.getResultList();                                
            }
            
            for(Paint paint:paints)
            {
                paint.getPaintCategories().size();
                paint.getTags().size();
            }
            
            Collections.sort(paints, new Comparator<Paint>()
            {
                public int compare(Paint pe1, Paint pe2) {
                    return pe1.getColourCode().compareTo(pe2.getColourCode());
                }
            });
            
            return paints;
        }
    }
    
    
    
    @Override
    public List<Paint> filterPaintsByTags(List<Long> tagIds, String condition)
    {
        List<Paint> paints = new ArrayList<>();
        
        if(tagIds == null || tagIds.isEmpty() || (!condition.equals("AND") && !condition.equals("OR")))
        {
            return paints;
        }
        else
        {
            if(condition.equals("OR"))
            {
                Query query = em.createQuery("SELECT DISTINCT pe FROM Paint pe, IN (pe.tagEntities) te WHERE te.tagId IN :inTagIds ORDER BY pe.colourCode ASC");
                query.setParameter("inTagIds", tagIds);
                paints = query.getResultList();                                                          
            }
            else // AND
            {
                String selectClause = "SELECT pe FROM Paint pe";
                String whereClause = "";
                Boolean firstTag = true;
                Integer tagCount = 1;

                for(Long tagId:tagIds)
                {
                    selectClause += ", IN (pe.tagEntities) te" + tagCount;

                    if(firstTag)
                    {
                        whereClause = "WHERE te1.tagId = " + tagId;
                        firstTag = false;
                    }
                    else
                    {
                        whereClause += " AND te" + tagCount + ".tagId = " + tagId; 
                    }
                    
                    tagCount++;
                }
                
                String jpql = selectClause + " " + whereClause + " ORDER BY pe.colourCode ASC";
                Query query = em.createQuery(jpql);
                paints = query.getResultList();                                
            }
            
            for(Paint paint:paints)
            {
                paint.getPaintCategories();
                paint.getTags().size();
            }
            
            Collections.sort(paints, new Comparator<Paint>()
            {
                public int compare(Paint pe1, Paint pe2) {
                    return pe1.getColourCode().compareTo(pe2.getColourCode());
                }
            });
            
            return paints;
        }
    }
    
    
    
    @Override
    public Paint retrievePaintByPaintId(Long paintId) throws PaintNotFoundException
    {
        Paint paint = em.find(Paint.class, paintId);
        
        if(paint != null)
        {
            paint.getPaintCategories();
            paint.getTags().size();
            
            return paint;
        }
        else
        {
            throw new PaintNotFoundException("Paint ID " + paintId + " does not exist!");
        }               
    }
    
    
    
    @Override
    public Paint retrievePaintByPaintColourCode(String colourCode) throws PaintNotFoundException
    {
        Query query = em.createQuery("SELECT p FROM Paint p WHERE p.colourCode = :inColourCode");
        query.setParameter("inColourCode", colourCode);
        
        try
        {
            Paint paint = (Paint)query.getSingleResult();            
            paint.getPaintCategories();
            paint.getTags().size();
            
            return paint;
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new PaintNotFoundException("Colour Code " + colourCode + " does not exist!");
        }
    }
    
    @Override
    public void updatePaint(Paint paint, List<Long> categoryIds, List<Long> tagIds) throws PaintNotFoundException, CategoryNotFoundException, TagNotFoundException, UpdatePaintException, InputDataValidationException
    {
        if(paint != null && paint.getPaintId()!= null)
        {
            Set<ConstraintViolation<Paint>>constraintViolations = validator.validate(paint);
        
            if(constraintViolations.isEmpty())
            {
                Paint paintToUpdate = retrievePaintByPaintId(paint.getPaintId());

                if(paintToUpdate.getColourCode().equals(paint.getColourCode()))
                {
                    //do the same for categories
                    if(categoryIds != null)
                    {
                        for(PaintCategory paintCategory:paintToUpdate.getPaintCategories())
                        {
                            paintCategory.getPaints().remove(paintToUpdate);
                        }
                        
                        paintToUpdate.getPaintCategories().clear();
                        
                        for(Long categoryId:categoryIds)
                        {
                            PaintCategory paintCategory = paintCategorySessionBeanLocal.retrieveCategoryByCategoryId(categoryId);
                            paintToUpdate.getPaintCategories().add(paintCategory);
                        }
                    }
                    
                    if(tagIds != null)
                    {
                        for(PaintTag paintTag:paintToUpdate.getTags())
                        {
                            paintTag.getPaints().remove(paintToUpdate);
                        }
                        
                        paintToUpdate.getTags().clear();
                        
                        for(Long tagId:tagIds)
                        {
                            PaintTag paintTag = paintTagSessionBeanLocal.retrieveTagByTagId(tagId);
                            paintToUpdate.getTags().add(paintTag);
                        }
                    }
                    
                    paintToUpdate.setName(paint.getName());
                    paintToUpdate.setColourCode(paint.getColourCode());
//                    paintToUpdate.setQuantityOnHand(paint.getQuantityOnHand());
//                    paintToUpdate.setReorderQuantity(paint.getReorderQuantity());
//                    paintToUpdate.setUnitPrice(paint.getUnitPrice());

//                    paintToUpdate.setPaintRating((paint.getPaintRating()));
                }
                else
                {
                    throw new UpdatePaintException("Colour Code of paint record to be updated does not match the existing record");
                }
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        else
        {
            throw new PaintNotFoundException("Paint ID not provided for paint to be updated");
        }
    }
    
    @Override
    public void deletePaint(Long paintId) throws PaintNotFoundException, DeletePaintException
    {
//        Paint paintToRemove = retrievePaintByPaintId(paintId);
//        
//        List<TransactionLineItem> transactionLineItems = transactionSessionBeanLocal.retrieveTransactionLineItemsByPaintId(paintId);
//        
//        if(transactionLineItems.isEmpty())
//        {
//            for(PaintCategory paintCategory:paintToRemove.getPaintCategories())
//            {
//                paintCategory.getPaints().remove(paintToRemove);
//            }
//            paintToRemove.getPaintCategories().clear();
//
//            
//            for(PaintTag paintTag:paintToRemove.getTags())
//            {
//                paintTag.getPaints().remove(paintToRemove);
//            }
//            
//            paintToRemove.getTags().clear();
//            
//            em.remove(paintToRemove);
//        }
//        else
//        {
//            throw new DeletePaintException("Paint ID " + paintId + " is associated with existing sale transaction line item(s) and cannot be deleted!");
//        }
    }
    
     
    private List<Paint> addSubCategoryPaints(PaintCategory paintCategory)
    {
        List<Paint> paints = new ArrayList<>();
                
        if(paintCategory.getSubCategoryEntities().isEmpty())
        {
            return paintCategory.getPaints();
        }
        else
        {
            for(PaintCategory subPaintCategory:paintCategory.getSubCategoryEntities())
            {
                paints.addAll(addSubCategoryPaints(subPaintCategory));
            }
            
            return paints;
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Paint>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
    
}
