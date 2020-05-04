/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.PaintService;
import java.util.List;

/**
 *
 * @author CHEN BINGSEN
 */
public class RetrieveAllPaintServicesRsp {

    
    
    private List<PaintService> paintServices;

    public RetrieveAllPaintServicesRsp() {
    }

    public RetrieveAllPaintServicesRsp(List<PaintService> paintServices) {
        this.paintServices = paintServices;
    }
    
    public List<PaintService> getPaintServices() {
        return paintServices;
    }

    public void setPaintServices(List<PaintService> paintServices) {
        this.paintServices = paintServices;
    }
    
}
