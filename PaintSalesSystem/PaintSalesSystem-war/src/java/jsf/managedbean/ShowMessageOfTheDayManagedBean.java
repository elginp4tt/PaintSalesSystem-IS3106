/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MessageOfTheDayEntitySessionBeanLocal;
import entity.Employee;
import entity.MessageOfTheDay;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.CloseEvent;

/**
 *
 * @author CHEN BINGSEN
 */
@Named(value = "showMessageOfTheDayManagedBean")
@ViewScoped
public class ShowMessageOfTheDayManagedBean implements Serializable
{

    @EJB(name = "MessageOfTheDayEntitySessionBeanLocal")
    private MessageOfTheDayEntitySessionBeanLocal messageOfTheDayEntitySessionBeanLocal;

    

    private List<MessageOfTheDay> allMotds;
    private Employee currentEmployee;
    
    public ShowMessageOfTheDayManagedBean() 
    {
    }
    
    
    @PostConstruct
    public void postCconstruct()
    {
        currentEmployee = (Employee)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentEmployeeEntity");
        allMotds = currentEmployee.getMotds();
    }
    
    
    public void removeMotd(CloseEvent event)
    {
        Long motdId = (Long)event.getComponent().getAttributes().get("motdId");
        MessageOfTheDay motdToDelete = messageOfTheDayEntitySessionBeanLocal.removeMotd(motdId, currentEmployee.getEmployeeId());
        allMotds.remove(motdToDelete);
        
    }
    
    
    public List<MessageOfTheDay> getAllMotds() {
        return allMotds;
    }

    public void setAllMotds(List<MessageOfTheDay> allMotds) {
        this.allMotds = allMotds;
    }
}
