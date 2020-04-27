/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

/**
 *
 * @author Elgin Patt
 */
public class CreateNewDeliveryRsp {
    private Long newDeliveryId;

    public CreateNewDeliveryRsp() {
    }

    public CreateNewDeliveryRsp(Long newDeliveryId) {
        this.newDeliveryId = newDeliveryId;
    }

    /**
     * @return the newDeliveryId
     */
    public Long getNewDeliveryId() {
        return newDeliveryId;
    }

    /**
     * @param newDeliveryId the newDeliveryId to set
     */
    public void setNewDeliveryId(Long newDeliveryId) {
        this.newDeliveryId = newDeliveryId;
    }
    
    
}
