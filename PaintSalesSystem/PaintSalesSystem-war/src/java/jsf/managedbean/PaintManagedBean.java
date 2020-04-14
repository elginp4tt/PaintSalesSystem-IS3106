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


@Named(value = "paintManagedBean")
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
        setPaints(paintSessionBeanLocal.retrieveAllPaints());
        setCategoryEntities(getPaintCategorySessionBeanLocal().retrieveAllLeafCategories());
        setTagEntities(getPaintTagSessionBeanLocal().retrieveAllTags());
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
            Paint pe = paintSessionBeanLocal.createNewPaint(getNewPaint(), getCategoryIdsNew(), getTagIdsNew());
            getPaints().add(pe);
            
            if(getFilteredPaints() != null)
            {
                getFilteredPaints().add(pe);
            }
            
            setNewPaint(new Paint());
            setCategoryIdsNew(null);
            setTagIdsNew(null);
            

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New paint created successfully (Paint ID: " + pe.getPaintId() + ")", null));
        }
        catch(InputDataValidationException | CreateNewPaintException | PaintExistException | UnknownPersistenceException ex)
        {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new paint: " + ex.getMessage(), null));
        }
    }
    
    
    
    public void doUpdatePaint(ActionEvent event)
    {
        setSelectedPaintToUpdate((Paint)event.getComponent().getAttributes().get("paintEntityToUpdate"));
        
        setCategoryIdsUpdate(new ArrayList<>());
        for(PaintCategory paintCategory:getSelectedPaintToUpdate().getPaintCategories())
        {
            getCategoryIdsUpdate().add(paintCategory.getPaintCategoryId());
        }
        
        setTagIdsUpdate(new ArrayList<>());
        for(PaintTag tagEntity:getSelectedPaintToUpdate().getTags())
        {
            getTagIdsUpdate().add(tagEntity.getTagId());
        }
    }
    
    
    
    public void updatePaint(ActionEvent event)
    {        
        try
        {
            paintSessionBeanLocal.updatePaint(getSelectedPaintToUpdate(), getCategoryIdsUpdate(), getTagIdsUpdate());
                        
            getSelectedPaintToUpdate().getPaintCategories().clear();
            for(PaintCategory c:getCategoryEntities()) {
                if(getCategoryIdsUpdate().contains(c.getPaintCategoryId())) {
                    getSelectedPaintToUpdate().getPaintCategories().add(c);
                }
            }
            
            getSelectedPaintToUpdate().getTags().clear();
            for(PaintTag te:getTagEntities())
            {
                if(getTagIdsUpdate().contains(te.getTagId()))
                {
                    getSelectedPaintToUpdate().getTags().add(te);
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
            
            getPaints().remove(paintEntityToDelete);
            
            if(getFilteredPaints() != null)
            {
                getFilteredPaints().remove(paintEntityToDelete);
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

    /**
     * @return the paintCategorySessionBeanLocal
     */
    public PaintCategorySessionBeanLocal getPaintCategorySessionBeanLocal() {
        return paintCategorySessionBeanLocal;
    }

    /**
     * @param paintCategorySessionBeanLocal the paintCategorySessionBeanLocal to set
     */
    public void setPaintCategorySessionBeanLocal(PaintCategorySessionBeanLocal paintCategorySessionBeanLocal) {
        this.paintCategorySessionBeanLocal = paintCategorySessionBeanLocal;
    }

    /**
     * @return the paintTagSessionBeanLocal
     */
    public PaintTagSessionBeanLocal getPaintTagSessionBeanLocal() {
        return paintTagSessionBeanLocal;
    }

    /**
     * @param paintTagSessionBeanLocal the paintTagSessionBeanLocal to set
     */
    public void setPaintTagSessionBeanLocal(PaintTagSessionBeanLocal paintTagSessionBeanLocal) {
        this.paintTagSessionBeanLocal = paintTagSessionBeanLocal;
    }

    /**
     * @return the viewPaintManagedBean
     */
    public ViewPaintManagedBean getViewPaintManagedBean() {
        return viewPaintManagedBean;
    }

    /**
     * @param viewPaintManagedBean the viewPaintManagedBean to set
     */
    public void setViewPaintManagedBean(ViewPaintManagedBean viewPaintManagedBean) {
        this.viewPaintManagedBean = viewPaintManagedBean;
    }

    /**
     * @return the paints
     */
    public List<Paint> getPaints() {
        return paints;
    }

    /**
     * @param paints the paints to set
     */
    public void setPaints(List<Paint> paints) {
        this.paints = paints;
    }

    /**
     * @return the filteredPaints
     */
    public List<Paint> getFilteredPaints() {
        return filteredPaints;
    }

    /**
     * @param filteredPaints the filteredPaints to set
     */
    public void setFilteredPaints(List<Paint> filteredPaints) {
        this.filteredPaints = filteredPaints;
    }

    /**
     * @return the newPaint
     */
    public Paint getNewPaint() {
        return newPaint;
    }

    /**
     * @param newPaint the newPaint to set
     */
    public void setNewPaint(Paint newPaint) {
        this.newPaint = newPaint;
    }

    /**
     * @return the categoryIdsNew
     */
    public List<Long> getCategoryIdsNew() {
        return categoryIdsNew;
    }

    /**
     * @param categoryIdsNew the categoryIdsNew to set
     */
    public void setCategoryIdsNew(List<Long> categoryIdsNew) {
        this.categoryIdsNew = categoryIdsNew;
    }

    /**
     * @return the tagIdsNew
     */
    public List<Long> getTagIdsNew() {
        return tagIdsNew;
    }

    /**
     * @param tagIdsNew the tagIdsNew to set
     */
    public void setTagIdsNew(List<Long> tagIdsNew) {
        this.tagIdsNew = tagIdsNew;
    }

    /**
     * @return the categoryEntities
     */
    public List<PaintCategory> getCategoryEntities() {
        return categoryEntities;
    }

    /**
     * @param categoryEntities the categoryEntities to set
     */
    public void setCategoryEntities(List<PaintCategory> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }

    /**
     * @return the tagEntities
     */
    public List<PaintTag> getTagEntities() {
        return tagEntities;
    }

    /**
     * @param tagEntities the tagEntities to set
     */
    public void setTagEntities(List<PaintTag> tagEntities) {
        this.tagEntities = tagEntities;
    }

    /**
     * @return the selectedPaintToUpdate
     */
    public Paint getSelectedPaintToUpdate() {
        return selectedPaintToUpdate;
    }

    /**
     * @param selectedPaintToUpdate the selectedPaintToUpdate to set
     */
    public void setSelectedPaintToUpdate(Paint selectedPaintToUpdate) {
        this.selectedPaintToUpdate = selectedPaintToUpdate;
    }

    /**
     * @return the categoryIdsUpdate
     */
    public List<Long> getCategoryIdsUpdate() {
        return categoryIdsUpdate;
    }

    /**
     * @param categoryIdsUpdate the categoryIdsUpdate to set
     */
    public void setCategoryIdsUpdate(List<Long> categoryIdsUpdate) {
        this.categoryIdsUpdate = categoryIdsUpdate;
    }

    /**
     * @return the tagIdsUpdate
     */
    public List<Long> getTagIdsUpdate() {
        return tagIdsUpdate;
    }

    /**
     * @param tagIdsUpdate the tagIdsUpdate to set
     */
    public void setTagIdsUpdate(List<Long> tagIdsUpdate) {
        this.tagIdsUpdate = tagIdsUpdate;
    }

}