/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.PaintCategorySessionBeanLocal;
import entity.PaintCategory;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewCategoryException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;
import util.exception.UpdateCategoryException;

/**
 *
 * @author matto
 */
@Named(value = "paintCategoryManagementManagedBean")
@ViewScoped
public class PaintCategoryManagementManagedBean implements Serializable {
    
    @EJB
    private PaintCategorySessionBeanLocal paintCategorySessionBeanLocal;
    
    @Inject
    private ViewPaintCategoryManagedBean viewPaintCategoryManagedBean;

    
    private List<PaintCategory> paintCategories;
    private PaintCategory newPaintCategory;
    private Long newPaintCategoryParentCategoryId;
    private PaintCategory selectedPaintCategoryToUpdate;
    private Long selectedPaintCategoryToUpdateParentCategoryId;

    /**
     * Creates a new instance of PaintCategoryManagementManagedBean
     */
    public PaintCategoryManagementManagedBean() {
        newPaintCategory = new PaintCategory();
    }
    
    @PostConstruct
    public void postConstruct() {
        this.setPaintCategories(getPaintCategorySessionBeanLocal().retrieveAllCategories());
    }
    
    public void viewPaintCategoryDetails(ActionEvent event) throws IOException {
        Long paintCategoryToView = (Long)event.getComponent().getAttributes().get("paintCategoryId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("paintCategoryToView", paintCategoryToView);
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewPaintCategories.xhtml");
    }
    
    public void createNewPaintCategory(ActionEvent event) {
        try {
            if (this.newPaintCategoryParentCategoryId == -1) {
                this.newPaintCategoryParentCategoryId = null;
            }
            System.out.println("************" + this.newPaintCategory.getCategoryName() + this.newPaintCategoryParentCategoryId);
            PaintCategory pc = getPaintCategorySessionBeanLocal().createNewPaintCategory(this.newPaintCategory, this.newPaintCategoryParentCategoryId);
            getPaintCategories().add(pc);
            System.out.println("*************************" + pc.getCategoryName() + "ID: " + pc.getParentCategoryEntity().getPaintCategoryId());
            setNewPaintCategory(new PaintCategory());
            setNewPaintCategoryParentCategoryId(null);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New paint category created successfully (Paint Category ID: " + pc.getPaintCategoryId() + ")", null));
        } catch (CreateNewCategoryException | InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating the new paint category: " + ex.getMessage(), null));
        }
    }
    
    public void dpUpdatePaintCategory(ActionEvent event) {
        this.selectedPaintCategoryToUpdate = (PaintCategory)event.getComponent().getAttributes().get("paintCategoryToUpdate");
        this.selectedPaintCategoryToUpdateParentCategoryId = (Long)event.getComponent().getAttributes().get("paintCategoryToUpdateParentCategoryId");
    }
    
    public void updatePaintCategory(ActionEvent event) {
        try {
            paintCategorySessionBeanLocal.updateCategory(this.selectedPaintCategoryToUpdate, this.selectedPaintCategoryToUpdateParentCategoryId);
            
            setSelectedPaintCategoryToUpdate(null);
            setNewPaintCategoryParentCategoryId(null);
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Paint Category updated successfully", null));
        } catch (CategoryNotFoundException | InputDataValidationException | UpdateCategoryException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating paint category: " + ex.getMessage(), null));
        } catch(Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred: " + ex.getMessage(), null));
        }
    }
    
    public void deletePaintCategory(ActionEvent event) {
        try {
            PaintCategory paintCategoryToDelete = (PaintCategory) event.getComponent().getAttributes().get("paintCategoryToDelete");
            paintCategorySessionBeanLocal.deleteCategory(paintCategoryToDelete.getPaintCategoryId());
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Paint category deleted successfully", null));
        } catch (CategoryNotFoundException | DeleteCategoryException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting paint: " + ex.getMessage(), null));
        } catch(Exception ex) {
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
     * @return the paintCategories
     */
    public List<PaintCategory> getPaintCategories() {
        return paintCategories;
    }

    /**
     * @param paintCategories the paintCategories to set
     */
    public void setPaintCategories(List<PaintCategory> paintCategories) {
        this.paintCategories = paintCategories;
    }

    /**
     * @return the newPaintCategory
     */
    public PaintCategory getNewPaintCategory() {
        return newPaintCategory;
    }

    /**
     * @param newPaintCategory the newPaintCategory to set
     */
    public void setNewPaintCategory(PaintCategory newPaintCategory) {
        this.newPaintCategory = newPaintCategory;
    }

    /**
     * @return the newPaintCategoryParentCategoryId
     */
    public Long getNewPaintCategoryParentCategoryId() {
        return newPaintCategoryParentCategoryId;
    }

    /**
     * @param newPaintCategoryParentCategoryId the newPaintCategoryParentCategoryId to set
     */
    public void setNewPaintCategoryParentCategoryId(Long newPaintCategoryParentCategoryId) {
        this.newPaintCategoryParentCategoryId = newPaintCategoryParentCategoryId;
    }

    /**
     * @return the selectedPaintCategoryToUpdate
     */
    public PaintCategory getSelectedPaintCategoryToUpdate() {
        return selectedPaintCategoryToUpdate;
    }

    /**
     * @param selectedPaintCategoryToUpdate the selectedPaintCategoryToUpdate to set
     */
    public void setSelectedPaintCategoryToUpdate(PaintCategory selectedPaintCategoryToUpdate) {
        this.selectedPaintCategoryToUpdate = selectedPaintCategoryToUpdate;
    }

    /**
     * @return the selectedPaintCategoryToUpdateParentCategoryId
     */
    public Long getSelectedPaintCategoryToUpdateParentCategoryId() {
        return selectedPaintCategoryToUpdateParentCategoryId;
    }

    /**
     * @param selectedPaintCategoryToUpdateParentCategoryId the selectedPaintCategoryToUpdateParentCategoryId to set
     */
    public void setSelectedPaintCategoryToUpdateParentCategoryId(Long selectedPaintCategoryToUpdateParentCategoryId) {
        this.selectedPaintCategoryToUpdateParentCategoryId = selectedPaintCategoryToUpdateParentCategoryId;
    }

    /**
     * @return the viewPaintCategoryManagedBean
     */
    public ViewPaintCategoryManagedBean getViewPaintCategoryManagedBean() {
        return viewPaintCategoryManagedBean;
    }

    /**
     * @param viewPaintCategoryManagedBean the viewPaintCategoryManagedBean to set
     */
    public void setViewPaintCategoryManagedBean(ViewPaintCategoryManagedBean viewPaintCategoryManagedBean) {
        this.viewPaintCategoryManagedBean = viewPaintCategoryManagedBean;
    }
}
