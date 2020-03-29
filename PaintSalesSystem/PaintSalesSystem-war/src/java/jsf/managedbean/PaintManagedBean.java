package jsf.managedbean;

import ejb.session.stateless.PaintCategorySessionBeanLocal;
import ejb.session.stateless.PaintSessionBeanLocal;
import ejb.session.stateless.PaintTagSessionBeanLocal;
import entity.Paint;
import entity.PaintCategory;
import entity.PaintTag;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import util.exception.CreateNewPaintException;
import util.exception.DeletePaintException;
import util.exception.InputDataValidationException;
import util.exception.PaintExistException;
import util.exception.PaintNotFoundException;
import util.exception.UnknownPersistenceException;



@Named
@ViewScoped

public class PaintManagedBean implements Serializable
{
    @EJB
    private PaintSessionBeanLocal paintSessionBeanLocal;
    @EJB
    private PaintCategorySessionBeanLocal paintCategorySessionBeanLocal;
    @EJB
    private PaintTagSessionBeanLocal paintTagSessionBeanLocal;
    
    @Inject
    private ViewPaintManagedBean viewPaintManagedBean;
    
    private List<Paint> paints;
    private List<Paint> filteredPaints;
    
    private Paint newPaint;
    private List<Long> categoryIdsNew;
    private List<Long> tagIdsNew;
    private List<PaintCategory> categoryEntities;
    private List<PaintTag> tagEntities;    
    
    private Paint selectedPaintToUpdate;
    private List<Long> categoryIdsUpdate;
    private List<Long> tagIdsUpdate;
    
    
    
    public PaintManagedBean()
    {
        newPaint = new Paint();
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        paints = paintSessionBeanLocal.retrieveAllPaints();
        setCategoryEntities(paintCategorySessionBeanLocal.retrieveAllLeafCategories());
        tagEntities = paintTagSessionBeanLocal.retrieveAllTags();
    }
    
    
    
    public void viewPaintDetails(ActionEvent event) throws IOException
    {
        Long paintIdToView = (Long)event.getComponent().getAttributes().get("paintId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("paintIdToView", paintIdToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewPaintDetails.xhtml");
    }
    
    
    
    public void createNewPaint(ActionEvent event)
    {                      
        
        try
        {
            Paint pe = paintSessionBeanLocal.createNewPaint(newPaint, categoryIdsNew, tagIdsNew);
            paints.add(pe);
            
            if(filteredPaints != null)
            {
                filteredPaints.add(pe);
            }
            
            newPaint = new Paint();
            categoryIdsNew = null;
            tagIdsNew = null;
            

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New paint created successfully (Paint ID: " + pe.getPaintId() + ")", null));
        }
        catch(InputDataValidationException | CreateNewPaintException | PaintExistException | UnknownPersistenceException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new paint: " + ex.getMessage(), null));
        }
    }
    
    
    
    public void doUpdatePaint(ActionEvent event)
    {
        selectedPaintToUpdate = (Paint)event.getComponent().getAttributes().get("paintEntityToUpdate");
        
        categoryIdsUpdate = new ArrayList<>();
        for(PaintCategory paintCategory:selectedPaintToUpdate.getPaintCategories())
        {
            categoryIdsUpdate.add(paintCategory.getPaintCategoryId());
        }
        
        tagIdsUpdate = new ArrayList<>();
        for(PaintTag tagEntity:selectedPaintToUpdate.getTags())
        {
            tagIdsUpdate.add(tagEntity.getTagId());
        }
    }
    
    
    
    public void updatePaint(ActionEvent event)
    {        
        try
        {
            paintSessionBeanLocal.updatePaint(selectedPaintToUpdate, categoryIdsUpdate, tagIdsUpdate);
                        
            selectedPaintToUpdate.getPaintCategories().clear();
            for(PaintCategory c:categoryEntities) {
                if(categoryIdsUpdate.contains(c.getPaintCategoryId())) {
                    selectedPaintToUpdate.getPaintCategories().add(c);
                }
            }
            
            selectedPaintToUpdate.getTags().clear();
            for(PaintTag te:tagEntities)
            {
                if(tagIdsUpdate.contains(te.getTagId()))
                {
                    selectedPaintToUpdate.getTags().add(te);
                }                
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Paint updated successfully", null));
        }
        catch(PaintNotFoundException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating paint: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    
    
    public void deletePaint(ActionEvent event)
    {
        try
        {
            Paint paintEntityToDelete = (Paint)event.getComponent().getAttributes().get("paintEntityToDelete");
            paintSessionBeanLocal.deletePaint(paintEntityToDelete.getPaintId());
            
            paints.remove(paintEntityToDelete);
            
            if(filteredPaints != null)
            {
                filteredPaints.remove(paintEntityToDelete);
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Paint deleted successfully", null));
        }
        catch(PaintNotFoundException | DeletePaintException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting paint: " + ex.getMessage(), null));
        }
        catch(Exception ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }

    
    
    public ViewPaintManagedBean getViewPaintManagedBean() {
        return viewPaintManagedBean;
    }

    public void setViewPaintManagedBean(ViewPaintManagedBean viewPaintManagedBean) {
        this.viewPaintManagedBean = viewPaintManagedBean;
    }
    
    public List<Paint> getPaintEntities() {
        return paints;
    }

    public void setPaintEntities(List<Paint> paints) {
        this.paints = paints;
    }

    public List<Paint> getFilteredPaintEntities() {
        return filteredPaints;
    }

    public void setFilteredPaintEntities(List<Paint> filteredPaints) {
        this.filteredPaints = filteredPaints;
    }

    public Paint getNewPaint() {
        return newPaint;
    }

    public void setNewPaint(Paint newPaint) {
        this.newPaint = newPaint;
    }

    public List<Long> getTagIdsNew() {
        return tagIdsNew;
    }

    public void setTagIdsNew(List<Long> tagIdsNew) {
        this.tagIdsNew = tagIdsNew;
    }

    public List<PaintCategory> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<PaintCategory> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }

    public List<PaintTag> getTags() {
        return tagEntities;
    }

    public void setTagEntities(List<PaintTag> tagEntities) {
        this.tagEntities = tagEntities;
    }
    
    public Paint getSelectedPaintToUpdate() {
        return selectedPaintToUpdate;
    }

    public void setSelectedPaintToUpdate(Paint selectedPaintToUpdate) {
        this.selectedPaintToUpdate = selectedPaintToUpdate;
    }

    public List<Long> getTagIdsUpdate() {
        return tagIdsUpdate;
    }

    public void setTagIdsUpdate(List<Long> tagIdsUpdate) {
        this.tagIdsUpdate = tagIdsUpdate;
    }    
}