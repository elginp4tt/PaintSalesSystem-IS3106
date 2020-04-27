/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Paint;

/**
 *
 * @author Elgin Patt
 */
public class RetrievePaintRsp {
    private Paint paint;

    public RetrievePaintRsp() {
    }

    public RetrievePaintRsp(Paint paint) {
        this.paint = paint;
    }

    /**
     * @return the paint
     */
    public Paint getPaint() {
        return paint;
    }

    /**
     * @param paint the paint to set
     */
    public void setPaint(Paint paint) {
        this.paint = paint;
    }
    
}
