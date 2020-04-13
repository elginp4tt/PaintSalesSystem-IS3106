/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.PaintCategorySessionBeanLocal;
import ejb.session.stateless.PaintSessionBeanLocal;
import entity.Paint;
import entity.PaintCategory;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author matto
 */
@Named(value = "filterPaintsByCategoriesManagedBean")
@ViewScoped
public class FilterPaintsByCategoriesManagedBean implements Serializable
{
    @EJB
    private PaintCategorySessionBeanLocal paintCategorySessionBeanLocal;
    @EJB
    private PaintSessionBeanLocal paintSessionBeanLocal;
    
    @Inject
    private ViewPaintManagedBean viewPaintManagedBean;
    
    private TreeNode treeNode;
    private TreeNode selectedTreeNode;
    private String condition;
    private List<Long> selectedCategoryIds;
    private List<SelectItem> selectItems;
    private List<Paint> paints;
    
    public FilterPaintsByCategoriesManagedBean() {
        condition = "OR";
    }
    
    @PostConstruct
    public void postConstruct()
    {
        List<PaintCategory> categoryEntities = paintCategorySessionBeanLocal.retrieveAllRootCategories();
        treeNode = new DefaultTreeNode("Root", null);
        
        for(PaintCategory categoryEntity:categoryEntities)
        {
            createTreeNode(categoryEntity, treeNode);
        }
        
        Long selectedCategoryId = (Long)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("paintFilterCategory");
        
        if(selectedCategoryId != null)
        {
            for(TreeNode tn:treeNode.getChildren())
            {
                PaintCategory ce = (PaintCategory)tn.getData();

                if(ce.getPaintCategoryId().equals(selectedCategoryId))
                {
                    selectedTreeNode = tn;
                    break;
                }
                else
                {
                    selectedTreeNode = searchTreeNode(selectedCategoryId, tn);
                }            
            }
        }
        
        filterPaint();
    }
    
    
    
    public void filterPaint()
    {
        if(selectedTreeNode != null)
        {               
            if(selectedCategoryIds != null &&  selectedCategoryIds.size() > 0)
            {
                paints = paintSessionBeanLocal.filterPaintsByCategories(getSelectedCategoryIds(), getCondition());
            }
            else
            {
                paints = paintSessionBeanLocal.retrieveAllPaints();
            }
        }
        else
        {
            setPaints(paintSessionBeanLocal.retrieveAllPaints());
        }
    }
    
    
    
    public void viewPaintDetails(ActionEvent event) throws IOException
    {
        Long paintIdToView = (Long)event.getComponent().getAttributes().get("paintId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("paintIdToView", paintIdToView);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("backMode", "filterPaintsByCategory");
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewPaintDetails.xhtml");
    }
    
    
    
    private void createTreeNode(PaintCategory categoryEntity, TreeNode parentTreeNode)
    {
        TreeNode treeNode = new DefaultTreeNode(categoryEntity, parentTreeNode);
                
        for(PaintCategory ce:categoryEntity.getSubCategoryEntities())
        {
            createTreeNode(ce, treeNode);
        }
    }
    
    
    
    private TreeNode searchTreeNode(Long selectedCategoryId, TreeNode treeNode)
    {
        for(TreeNode tn:treeNode.getChildren())
        {
            PaintCategory ce = (PaintCategory)tn.getData();
            
            if(ce.getPaintCategoryId().equals(selectedCategoryId))
            {
                return tn;
            }
            else
            {
                return searchTreeNode(selectedCategoryId, tn);
            }            
        }
        
        return null;
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
     * @return the selectedCategoryIds
     */
    public List<Long> getSelectedCategoryIds() {
        return selectedCategoryIds;
    }

    /**
     * @param selectedCategoryIds the selectedCategoryIds to set
     */
    public void setSelectedCategoryIds(List<Long> selectedCategoryIds) {
        this.selectedCategoryIds = selectedCategoryIds;
    }

    /**
     * @return the selectItems
     */
    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    /**
     * @param selectItems the selectItems to set
     */
    public void setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }

    /**
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }

    /**
     * @param condition the condition to set
     */
    public void setCondition(String condition) {
        this.condition = condition;
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
}
