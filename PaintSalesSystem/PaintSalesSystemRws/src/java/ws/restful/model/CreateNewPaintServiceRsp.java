/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.PaintService;
import entity.PaintServiceTransaction;

/**
 *
 * @author Elgin Patt
 */
public class CreateNewPaintServiceRsp {

    
    
    private PaintService paintService;
    private PaintServiceTransaction paintServiceTransaction;

    public CreateNewPaintServiceRsp() {
    }

    public CreateNewPaintServiceRsp(PaintService paintService, PaintServiceTransaction paintServiceTransaction) {
        this.paintService = paintService;
        this.paintServiceTransaction = paintServiceTransaction;
    }

    
    
    
    public PaintService getPaintService() {
        return paintService;
    }

    public void setPaintService(PaintService paintService) {
        this.paintService = paintService;
    }

    public PaintServiceTransaction getPaintServiceTransaction() {
        return paintServiceTransaction;
    }

    public void setPaintServiceTransaction(PaintServiceTransaction paintServiceTransaction) {
        this.paintServiceTransaction = paintServiceTransaction;
    }
    
}
