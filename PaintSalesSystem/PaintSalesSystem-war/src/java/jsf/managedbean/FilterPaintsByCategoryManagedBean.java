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
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import util.exception.CategoryNotFoundException;

/**
 *
 * @author matto
 */
@Named(value = "filterPaintsByCategoryManagedBean")
@ViewScoped
public class FilterPaintsByCategoryManagedBean implements Serializable
{

    @EJB
    private PaintCategorySessionBeanLocal paintCategorySessionBeanLocal;
    @EJB
    private PaintSessionBeanLocal paintSessionBeanLocal;
    
    @Inject
    private ViewPaintManagedBean viewPaintManagedBean;
    
    private TreeNode treeNode;
    private TreeNode selectedTreeNode;
    
    private List<Paint> paints;
    
    public FilterPaintsByCategoryManagedBean() {
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
            try
            {
                PaintCategory ce = (PaintCategory)selectedTreeNode.getData();
                setPaints(paintSessionBeanLocal.filterPaintsByCategory(ce.getPaintCategoryId()));
            }
            catch(CategoryNotFoundException ex)
            {
                setPaints(paintSessionBeanLocal.retrieveAllPaints());
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
    
}
