/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.PaintTagSessionBeanLocal;
import entity.PaintTag;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import util.exception.CreateNewTagException;
import util.exception.DeleteTagException;
import util.exception.InputDataValidationException;
import util.exception.TagNotFoundException;
import util.exception.UpdateTagException;

/**
 *
 * @author matto
 */
@Named(value = "paintTagManagementManagedBean")
@ViewScoped
public class PaintTagManagementManagedBean implements Serializable{

    @EJB
    private PaintTagSessionBeanLocal paintTagSessionBeanLocal;

    @Inject
    private ViewPaintTagManagedBean viewPaintTagManagedBean;
    
    private List<PaintTag> paintTags;
    private PaintTag selectedPaintTagToUpdate;
    private PaintTag selectedPaintTagToDelete;
    private PaintTag newPaintTag;
    /**
     * Creates a new instance of PaintTagManagementManagedBean
     */
    public PaintTagManagementManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct() {
        this.paintTags = paintTagSessionBeanLocal.retrieveAllTags();
        this.setNewPaintTag(new PaintTag());
    }
    
    public void createNewPaintTag(ActionEvent event){
        try{
            PaintTag pt = paintTagSessionBeanLocal.createNewPaintTag(newPaintTag);
            this.paintTags.add(newPaintTag);
            this.newPaintTag = new PaintTag();
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "New paint tag created successfully (Paint Tag ID: " + pt.getTagId()+ ")", null));

        } catch (CreateNewTagException | InputDataValidationException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while creating Paint Tag: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred while creating Paint Tag: " + ex.getMessage(), null));
        }
    }
    
    public void doUpdatePaintTag(ActionEvent event) {
        this.selectedPaintTagToUpdate = (PaintTag) event.getComponent().getAttributes().get("paintTagToUpdate");
    }
    
    public void updatePaintTag(ActionEvent event) {
        try {
            paintTagSessionBeanLocal.updateTag(selectedPaintTagToUpdate);
            this.selectedPaintTagToUpdate = null;
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Paint Tag updated successfully!", null));
        } catch (InputDataValidationException | TagNotFoundException | UpdateTagException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while updating Paint Tag: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred while updating Paint Tag: " + ex.getMessage(), null));
        }
    }
    
    public void deletePaintTag(ActionEvent event) {
        this.selectedPaintTagToDelete = (PaintTag) event.getComponent().getAttributes().get("paintTagToDelete");
        try {
            paintTagSessionBeanLocal.deleteTag(selectedPaintTagToDelete.getTagId());
            
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Paint Tag deleted successfully!", null));
        } catch (DeleteTagException | TagNotFoundException ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An error has occurred while deleting Paint Tag: " + ex.getMessage(), null));
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "An unexpected error has occurred while deleting Paint Tag: " + ex.getMessage(), null));
        } 
    }

    /**
     * @return the paintTags
     */
    public List<PaintTag> getPaintTags() {
        return paintTags;
    }

    /**
     * @param paintTags the paintTags to set
     */
    public void setPaintTags(List<PaintTag> paintTags) {
        this.paintTags = paintTags;
    }

    /**
     * @return the selectedPaintTagToUpdate
     */
    public PaintTag getSelectedPaintTagToUpdate() {
        return selectedPaintTagToUpdate;
    }

    /**
     * @param selectedPaintTagToUpdate the selectedPaintTagToUpdate to set
     */
    public void setSelectedPaintTagToUpdate(PaintTag selectedPaintTagToUpdate) {
        this.selectedPaintTagToUpdate = selectedPaintTagToUpdate;
    }

    /**
     * @return the selectedPaintTagToDelete
     */
    public PaintTag getSelectedPaintTagToDelete() {
        return selectedPaintTagToDelete;
    }

    /**
     * @param selectedPaintTagToDelete the selectedPaintTagToDelete to set
     */
    public void setSelectedPaintTagToDelete(PaintTag selectedPaintTagToDelete) {
        this.selectedPaintTagToDelete = selectedPaintTagToDelete;
    }

    /**
     * @return the newPaintTag
     */
    public PaintTag getNewPaintTag() {
        return newPaintTag;
    }

    /**
     * @param newPaintTag the newPaintTag to set
     */
    public void setNewPaintTag(PaintTag newPaintTag) {
        this.newPaintTag = newPaintTag;
    }

    /**
     * @return the viewPaintTagManagedBean
     */
    public ViewPaintTagManagedBean getViewPaintTagManagedBean() {
        return viewPaintTagManagedBean;
    }

    /**
     * @param viewPaintTagManagedBean the viewPaintTagManagedBean to set
     */
    public void setViewPaintTagManagedBean(ViewPaintTagManagedBean viewPaintTagManagedBean) {
        this.viewPaintTagManagedBean = viewPaintTagManagedBean;
    }
    
}
