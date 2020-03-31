package jsf.managedbean;

import ejb.session.stateless.DeliveryEntitySessionBeanLocal;
import entity.Delivery;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author CHEN BINGSEN
 */
@Named(value = "deliveryManagementManagedBean")
@ViewScoped
public class DeliveryManagementManagedBean implements Serializable{

    @EJB
    private DeliveryEntitySessionBeanLocal deliveryEntitySessionBeanLocal;

    private List<Delivery> deliveries;
    private Delivery deliveryToView;
    
    
    
    
    public DeliveryManagementManagedBean() 
    {
        deliveryToView = new Delivery();
    }
    
    @PostConstruct
    public void postConstruct()
    {
        deliveries = deliveryEntitySessionBeanLocal.retrieveAllDelivery();
    }
    
    
    
}
