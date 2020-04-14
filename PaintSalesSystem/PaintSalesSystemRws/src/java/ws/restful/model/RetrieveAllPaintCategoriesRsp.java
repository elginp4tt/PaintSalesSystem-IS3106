/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.PaintCategory;
import java.util.List;

/**
 *
 * @author Elgin Patt
 */
public class RetrieveAllPaintCategoriesRsp {
    private List<PaintCategory> paintCategories;

    public RetrieveAllPaintCategoriesRsp() {
    }

    public RetrieveAllPaintCategoriesRsp(List<PaintCategory> paintCategories) {
        this.paintCategories = paintCategories;
    }

    /**
     * @return the paintCategories
     */
    public List<PaintCategory> getPaintCategories() {
        return paintCategories;
    }

    /**
     * @param paintCategories the paintCategories to set
     */
    public void setPaintCategories(List<PaintCategory> paintCategories) {
        this.paintCategories = paintCategories;
    }
    
    
}
