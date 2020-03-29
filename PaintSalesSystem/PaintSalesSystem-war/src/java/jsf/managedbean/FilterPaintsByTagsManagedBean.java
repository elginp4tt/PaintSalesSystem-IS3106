package jsf.managedbean;

import ejb.session.stateless.PaintSessionBeanLocal;
import ejb.session.stateless.PaintTagSessionBeanLocal;
import entity.Paint;
import entity.PaintTag;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;



@Named(value = "filterPaintsByTagsManagedBean")
@ViewScoped

public class FilterPaintsByTagsManagedBean implements Serializable
{
    @EJB
    private PaintTagSessionBeanLocal tagEntitySessionBeanLocal;
    @EJB
    private PaintSessionBeanLocal paintEntitySessionBeanLocal;
    
    @Inject
    private ViewPaintManagedBean viewPaintManagedBean;
    
    private String condition;
    private List<Long> selectedTagIds;
    private List<SelectItem> selectItems;
    private List<Paint> paintEntities;
    
    
    
    public FilterPaintsByTagsManagedBean()
    {
        condition = "OR";
    }
    
    
    
    @PostConstruct
    public void postConstruct()
    {
        List<PaintTag> tagEntities = tagEntitySessionBeanLocal.retrieveAllTags();
        selectItems = new ArrayList<>();
        
        for(PaintTag tagEntity:tagEntities)
        {
            selectItems.add(new SelectItem(tagEntity.getTagId(), tagEntity.getName(), tagEntity.getName()));
        }
        
        
        // Optional demonstration of the use of custom converter
        // FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("PaintTagConverter_tagEntities", tagEntities);
        
        condition = (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("paintFilterCondition");        
        selectedTagIds = (List<Long>)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("paintFilterTags");
        
        filterPaint();
    }
    
    
    
    @PreDestroy
    public void preDestroy()
    {
        // FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("PaintTagConverter_tagEntities", null);
        // FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("PaintTagConverter_tagEntities", null);
    }
    
    
    
    public void filterPaint()
    {        
        if(selectedTagIds != null && selectedTagIds.size() > 0)
        {
            paintEntities = paintEntitySessionBeanLocal.filterPaintsByTags(selectedTagIds, condition);
        }
        else
        {
            paintEntities = paintEntitySessionBeanLocal.retrieveAllPaints();
        }
    }
    
    
    
    public void viewPaintDetails(ActionEvent event) throws IOException
    {
        Long paintIdToView = (Long)event.getComponent().getAttributes().get("paintId");
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("paintIdToView", paintIdToView);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("backMode", "filterPaintsByTags");
        FacesContext.getCurrentInstance().getExternalContext().redirect("viewPaintDetails.xhtml");
    }

    
    
    public ViewPaintManagedBean getViewPaintManagedBean() {
        return viewPaintManagedBean;
    }

    public void setViewPaintManagedBean(ViewPaintManagedBean viewPaintManagedBean) {
        this.viewPaintManagedBean = viewPaintManagedBean;
    }
    
    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) 
    {
        this.condition = condition;
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paintFilterCondition", condition);
    }
    
    public List<Long> getSelectedTagIds() {
        return selectedTagIds;
    }

    public void setSelectedTagIds(List<Long> selectedTagIds) 
    {
        this.selectedTagIds = selectedTagIds;
        
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("paintFilterTags", selectedTagIds);
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public void setSelectItems(List<SelectItem> selectItems) {
        this.selectItems = selectItems;
    }    

    public List<Paint> getPaintEntities() {
        return paintEntities;
    }

    public void setPaintEntities(List<Paint> paintEntities) {
        this.paintEntities = paintEntities;
    }
}
