/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MessageOfTheDayEntitySessionBeanLocal;
import entity.MessageOfTheDay;
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
@Named(value = "showMessageOfTheDayManagedBean")
@ViewScoped
public class ShowMessageOfTheDayManagedBean implements Serializable
{


    @EJB(name = "MessageOfTheDayEntitySessionBeanLocal")
    private MessageOfTheDayEntitySessionBeanLocal messageOfTheDayEntitySessionBeanLocal;

    private List<MessageOfTheDay> allMotds;
    
    public ShowMessageOfTheDayManagedBean() 
    {
    }
    
    
    @PostConstruct
    public void postCconstruct()
    {
        allMotds = messageOfTheDayEntitySessionBeanLocal.retrieveAllMessagesOfTheDay();
    }
    
    
    public List<MessageOfTheDay> getAllMotds() {
        return allMotds;
    }

    public void setAllMotds(List<MessageOfTheDay> allMotds) {
        this.allMotds = allMotds;
    }
}
