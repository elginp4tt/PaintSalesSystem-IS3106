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
public class CreateNewPaintRsp {
    
    private Long newPaintId;

    public CreateNewPaintRsp() {
    }

    public CreateNewPaintRsp(Long newPaintId) {
        this.newPaintId = newPaintId;
    }

    /**
     * @return the newPaintId
     */
    public Long getNewPaintId() {
        return newPaintId;
    }

    /**
     * @param newPaintId the newPaintId to set
     */
    public void setNewPaintId(Long newPaintId) {
        this.newPaintId = newPaintId;
    }
    
}
