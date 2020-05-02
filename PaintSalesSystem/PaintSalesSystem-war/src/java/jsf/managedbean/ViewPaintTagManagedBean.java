/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.PaintTag;
import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author matto
 */
@Named(value = "viewPaintTagManagedBean")
@ViewScoped
public class ViewPaintTagManagedBean implements Serializable{
    
    private PaintTag paintTagToView;
    /**
     * Creates a new instance of ViewPaintTagManagedBean
     */
    public ViewPaintTagManagedBean() {
    }

    /**
     * @return the paintTagToView
     */
    public PaintTag getPaintTagToView() {
        return paintTagToView;
    }

    /**
     * @param paintTagToView the paintTagToView to set
     */
    public void setPaintTagToView(PaintTag paintTagToView) {
        this.paintTagToView = paintTagToView;
    }
    
}
