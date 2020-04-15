/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.PaintService;
import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author CHEN BINGSEN
 */
@Named(value = "viewPaintServiceManagedBean")
@ViewScoped
public class ViewPaintServiceManagedBean implements Serializable
{

    
    private PaintService paintServiceToView;
    
    
    public ViewPaintServiceManagedBean() 
    {
        paintServiceToView = new PaintService();
    }

    public PaintService getPaintServiceToView() {
        return paintServiceToView;
    }

    public void setPaintServiceToView(PaintService paintServiceToView) {
        this.paintServiceToView = paintServiceToView;
    }
    
}
