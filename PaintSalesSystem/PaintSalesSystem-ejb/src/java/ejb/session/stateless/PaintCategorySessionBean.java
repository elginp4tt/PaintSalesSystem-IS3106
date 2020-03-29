package ejb.session.stateless;

import entity.PaintCategory;
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
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCategoryException;



@Stateless
@Local(PaintCategorySessionBeanLocal.class)

// Newly added in v5.0

public class PaintCategorySessionBean implements PaintCategorySessionBeanLocal
{
    @PersistenceContext(unitName = "PaintSalesSystem-ejbPU")
    private EntityManager em;
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public PaintCategorySessionBean()
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    
    @Override
    public PaintCategory createNewPaintCategory(PaintCategory newPaintCategory, Long parentCategoryId) throws InputDataValidationException, CreateNewCategoryException
    {
        Set<ConstraintViolation<PaintCategory>>constraintViolations = validator.validate(newPaintCategory);
        
        if(constraintViolations.isEmpty())
        {
            try
            {
                if(parentCategoryId != null)
                {
                    PaintCategory parentPaintCategory = retrieveCategoryByCategoryId(parentCategoryId);

                    if(!parentPaintCategory.getPaints().isEmpty())
                    {
                        throw new CreateNewCategoryException("Parent category cannot be associated with any product");
                    }

                    newPaintCategory.setParentCategoryEntity(parentPaintCategory);
                }
                
                em.persist(newPaintCategory);
                em.flush();

                return newPaintCategory;
            }
            catch(PersistenceException ex)
            {                
                if(ex.getCause() != null && 
                        ex.getCause().getCause() != null &&
                        ex.getCause().getCause().getClass().getSimpleName().equals("SQLIntegrityConstraintViolationException"))
                {
                    throw new CreateNewCategoryException("Category with same name already exist");
                }
                else
                {
                    throw new CreateNewCategoryException("An unexpected error has occurred: " + ex.getMessage());
                }
            }
            catch(Exception ex)
            {
                throw new CreateNewCategoryException("An unexpected error has occurred: " + ex.getMessage());
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    @Override
    public List<PaintCategory> retrieveAllCategories()
    {
        Query query = em.createQuery("SELECT pc FROM PaintCategory pc ORDER BY pc.name ASC");
        List<PaintCategory> categoryEntities = query.getResultList();
        
        for(PaintCategory paintCategory:categoryEntities)
        {
            paintCategory.getParentCategoryEntity();
            paintCategory.getSubCategoryEntities().size();
            paintCategory.getPaints().size();
        }
        
        return categoryEntities;
    }
    
    
    
    @Override
    public List<PaintCategory> retrieveAllRootCategories()
    {
        Query query = em.createQuery("SELECT pc FROM PaintCategory pc WHERE pc.parentPaintCategory IS NULL ORDER BY pc.name ASC");
        List<PaintCategory> rootCategoryEntities = query.getResultList();
        
        for(PaintCategory rootPaintCategory:rootCategoryEntities)
        {            
            lazilyLoadSubCategories(rootPaintCategory);
            
            rootPaintCategory.getPaints().size();
        }
        
        return rootCategoryEntities;
    }
    
    
    
    @Override
    public List<PaintCategory> retrieveAllLeafCategories()
    {
        Query query = em.createQuery("SELECT pc FROM PaintCategory pc WHERE pc.subCategoryEntities IS EMPTY ORDER BY pc.name ASC");
        List<PaintCategory> leafCategoryEntities = query.getResultList();
        
        for(PaintCategory leafPaintCategory:leafCategoryEntities)
        {            
            leafPaintCategory.getPaints().size();
        }
        
        return leafCategoryEntities;
    }
    
    
    
    @Override
    public List<PaintCategory> retrieveAllCategoriesWithoutPaint()
    {
        Query query = em.createQuery("SELECT pc FROM PaintCategory pc WHERE pc.productEntities IS EMPTY ORDER BY pc.name ASC");
        List<PaintCategory> rootCategoryEntities = query.getResultList();
        
        for(PaintCategory rootPaintCategory:rootCategoryEntities)
        {
            rootPaintCategory.getParentCategoryEntity();            
        }
        
        return rootCategoryEntities;
    }
    
    
    
    @Override
    public PaintCategory retrieveCategoryByCategoryId(Long categoryId) throws CategoryNotFoundException
    {
        PaintCategory paintCategory = em.find(PaintCategory.class, categoryId);
        
        if(paintCategory != null)
        {
            return paintCategory;
        }
        else
        {
            throw new CategoryNotFoundException("Paint Category ID " + categoryId + " does not exist!");
        }               
    }
    
    
    
    @Override
    public void updateCategory(PaintCategory paintCategory, Long parentCategoryId) throws InputDataValidationException, CategoryNotFoundException, UpdateCategoryException
    {
        Set<ConstraintViolation<PaintCategory>>constraintViolations = validator.validate(paintCategory);
        
        if(constraintViolations.isEmpty())
        {
            if(paintCategory.getPaintCategoryId()!= null)
            {
                PaintCategory paintCategoryToUpdate = retrieveCategoryByCategoryId(paintCategory.getPaintCategoryId());
                
                Query query = em.createQuery("SELECT pc FROM PaintCategory pc WHERE pc.name = :inName AND pc.categoryId <> :inCategoryId");
                query.setParameter("inName", paintCategory.getCategoryName());
                query.setParameter("inCategoryId", paintCategory.getPaintCategoryId());
                
                if(!query.getResultList().isEmpty())
                {
                    throw new UpdateCategoryException("The name of the category to be updated is duplicated");
                }
                
                paintCategoryToUpdate.setCategoryName(paintCategory.getCategoryName());
                paintCategoryToUpdate.setDescription(paintCategory.getDescription());                               
                
                if(parentCategoryId != null)
                {
                    if(paintCategoryToUpdate.getPaintCategoryId().equals(parentCategoryId))
                    {
                        throw new UpdateCategoryException("Category cannot be its own parent");
                    }
                    else if(paintCategoryToUpdate.getParentCategoryEntity()== null || (!paintCategoryToUpdate.getParentCategoryEntity().getPaintCategoryId().equals(parentCategoryId)))
                    {
                        PaintCategory parentPaintCategoryToUpdate = retrieveCategoryByCategoryId(parentCategoryId);
                        
                        if(!parentPaintCategoryToUpdate.getPaints().isEmpty())
                        {
                            throw new UpdateCategoryException("Parent category cannot have any product associated with it");
                        }
                        
                        paintCategoryToUpdate.setParentCategoryEntity(parentPaintCategoryToUpdate);
                    }
                }
                else
                {
                    paintCategoryToUpdate.setParentCategoryEntity(null);
                }                
            }
            else
            {
                throw new CategoryNotFoundException("Category ID not provided for category to be updated");
            }
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    @Override
    public void deleteCategory(Long categoryId) throws CategoryNotFoundException, DeleteCategoryException
    {
        PaintCategory paintCategoryToRemove = retrieveCategoryByCategoryId(categoryId);
        
        if(!paintCategoryToRemove.getSubCategoryEntities().isEmpty())
        {
            throw new DeleteCategoryException("Category ID " + categoryId + " is associated with existing sub-categories and cannot be deleted!");
        }
        else if(!paintCategoryToRemove.getPaints().isEmpty())
        {
            throw new DeleteCategoryException("Category ID " + categoryId + " is associated with existing products and cannot be deleted!");
        }
        else
        {
            paintCategoryToRemove.setParentCategoryEntity(null);
            
            em.remove(paintCategoryToRemove);
        }                
    }
    
    
    
    private void lazilyLoadSubCategories(PaintCategory paintCategory)
    {
        for(PaintCategory pc:paintCategory.getSubCategoryEntities())
        {
            lazilyLoadSubCategories(pc);
        }
    }
    
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<PaintCategory>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
