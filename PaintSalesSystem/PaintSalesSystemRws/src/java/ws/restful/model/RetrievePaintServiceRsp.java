/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.PaintService;

/**
 *
 * @author Elgin Patt
 */
public class RetrievePaintServiceRsp {
    private PaintService paintService;

    public RetrievePaintServiceRsp() {
    }

    public RetrievePaintServiceRsp(PaintService paintService) {
        this.paintService = paintService;
    }

    /**
     * @return the paintService
     */
    public PaintService getPaintService() {
        return paintService;
    }

    /**
     * @param paintService the paintService to set
     */
    public void setPaintService(PaintService paintService) {
        this.paintService = paintService;
    }
    
    
}
