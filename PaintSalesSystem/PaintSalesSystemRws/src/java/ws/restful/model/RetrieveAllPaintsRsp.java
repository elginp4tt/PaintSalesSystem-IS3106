/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.Paint;
import java.util.List;

/**
 *
 * @author Elgin Patt
 */
public class RetrieveAllPaintsRsp {
    private List<Paint> paints;

    public RetrieveAllPaintsRsp() {
    }

    public RetrieveAllPaintsRsp(List<Paint> paints) {
        this.paints = paints;
    }

    /**
     * @return the paints
     */
    public List<Paint> getPaints() {
        return paints;
    }

    /**
     * @param paints the paints to set
     */
    public void setPaints(List<Paint> paints) {
        this.paints = paints;
    }
    
}
