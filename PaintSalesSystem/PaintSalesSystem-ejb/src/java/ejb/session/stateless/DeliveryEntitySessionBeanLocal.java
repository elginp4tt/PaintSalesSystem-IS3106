/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Delivery;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteDeliveryException;
import util.exception.DeliveryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateDeliveryException;

/**
 *
 * @author CHEN BINGSEN
 */
@Local
public interface DeliveryEntitySessionBeanLocal {


    public List<Delivery> retrieveAllDelivery();

    public Delivery retrieveDeliveryByDeliveryId(Long deliveryId) throws DeliveryNotFoundException;

    public Long createNewDelivery(Delivery newDelivery) throws UnknownPersistenceException, InputDataValidationException;

    public void updateDelivery(Delivery delivery) throws DeliveryNotFoundException, InputDataValidationException;

    public void deleteDelivery(Long deliveryId) throws DeliveryNotFoundException, DeleteDeliveryException;
    
}
