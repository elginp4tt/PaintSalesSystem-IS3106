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
public class CreateNewPaintServiceRsp {
    private Long newPaintServiceId;

    public CreateNewPaintServiceRsp() {
    }

    public CreateNewPaintServiceRsp(Long newPaintServiceId) {
        this.newPaintServiceId = newPaintServiceId;
    }

    /**
     * @return the newPaintServiceId
     */
    public Long getNewPaintServiceId() {
        return newPaintServiceId;
    }

    /**
     * @param newPaintServiceId the newPaintServiceId to set
     */
    public void setNewPaintServiceId(Long newPaintServiceId) {
        this.newPaintServiceId = newPaintServiceId;
    }
    
    
}
