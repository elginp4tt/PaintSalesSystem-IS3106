/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.PaintCategory;
import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author matto
 */
@Named(value = "viewPaintCategoryManagedBean")
@ViewScoped
public class ViewPaintCategoryManagedBean implements Serializable {
    
    private PaintCategory paintCategoryToView;
    /**
     * Creates a new instance of ViewPaintCategoryManagedBean
     */
    public ViewPaintCategoryManagedBean() {
        paintCategoryToView = new PaintCategory();
    }

    /**
     * @return the paintCategoryToView
     */
    public PaintCategory getPaintCategoryToView() {
        return paintCategoryToView;
    }

    /**
     * @param paintCategoryToView the paintCategoryToView to set
     */
    public void setPaintCategoryToView(PaintCategory paintCategoryToView) {
        this.paintCategoryToView = paintCategoryToView;
    }
    
}
