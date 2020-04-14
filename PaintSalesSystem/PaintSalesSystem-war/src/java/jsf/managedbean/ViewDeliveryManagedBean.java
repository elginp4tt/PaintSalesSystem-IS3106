/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import entity.Delivery;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author CHEN BINGSEN
 */
@Named(value = "viewDeliveryManagedBean")
@ViewScoped

public class ViewDeliveryManagedBean implements Serializable
{

    
    private Delivery deliveryToView;
    
    
    public ViewDeliveryManagedBean() 
    {
        deliveryToView = new Delivery();
    }
    
    
    public Delivery getDeliveryToView() {
        return deliveryToView;
    }

    public void setDeliveryToView(Delivery deliveryToView) {
        this.deliveryToView = deliveryToView;
    }
     
}
