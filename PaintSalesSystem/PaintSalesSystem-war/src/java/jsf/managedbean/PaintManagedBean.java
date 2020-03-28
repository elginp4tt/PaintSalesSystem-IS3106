/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author matto
 */
@Named(value = "paintManagedBean")
@ViewScoped
public class PaintManagedBean implements Serializable {
    
    
    /**
     * Creates a new instance of PaintManagedBean
     */
    public PaintManagedBean() {
    }
    
    
}
