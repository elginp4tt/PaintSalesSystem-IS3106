/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.PaintService;

/**
 *
 * @author CHEN BINGSEN
 */
public class UpdatePaintServiceReq 
{

    public PaintService getPaintService() {
        return paintService;
    }

    

    public UpdatePaintServiceReq() {
    }

    public UpdatePaintServiceReq(PaintService paintService) {
        this.paintService = paintService;
    }
    
    
    public void setPaintService(PaintService paintService) {
        this.paintService = paintService;
    }
    private PaintService paintService;
    
}
