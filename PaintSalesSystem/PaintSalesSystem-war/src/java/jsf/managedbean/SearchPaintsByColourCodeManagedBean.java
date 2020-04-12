/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.PaintSessionBeanLocal;
import entity.Paint;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

/**
 *
 * @author matto
 */
@Named(value = "searchPaintsByColourCodeManagedBean")
@ViewScoped
public class SearchPaintsByColourCodeManagedBean implements Serializable {

    @EJB
    private PaintSessionBeanLocal paintSessionBeanLocal;
    
    @Inject
    private ViewPaintManagedBean viewPaintManagedBean;
    
    private String searchString;
    private List<Paint> paints;

    /**
     * Creates a new instance of SearchPaintsByColourCodeManagedBean
     */
    public SearchPaintsByColourCodeManagedBean() {
    }
    
    @PostConstruct
    public void postConstruct(){
        setSearchString((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("paintSearchString"));
        
        if(getSearchString() == null || getSearchString().trim().length() == 0) {
            setPaints(paintSessionBeanLocal.retrieveAllPaints());
        } else {
            setPaints(paintSessionBeanLocal.searchPaintsByName(getSearchString()));
        }
    }
    
    public void searchPaint() {
        if(getSearchString() == null || getSearchString().trim().length() == 0) {
            setPaints(paintSessionBeanLocal.retrieveAllPaints());
        } else {
            setPaints(paintSessionBeanLocal.searchPaintsByName(getSearchString()));
        }
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
     * @return the searchString
     */
    public String getSearchString() {
        return searchString;
    }

    /**
     * @param searchString the searchString to set
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
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
