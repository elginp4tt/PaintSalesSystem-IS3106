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
public class CreateNewPaintReq {
    
    private Paint newPaint;
    private List<Long> categoryIds;
    private List<Long> tagIds;

    public CreateNewPaintReq() {
    }

    public CreateNewPaintReq(Paint newPaint, List<Long> categoryIds, List<Long> tagIds) {
        this.newPaint = newPaint;
        this.categoryIds = categoryIds;
        this.tagIds = tagIds;
    }

    /**
     * @return the newPaint
     */
    public Paint getNewPaint() {
        return newPaint;
    }

    /**
     * @param newPaint the newPaint to set
     */
    public void setNewPaint(Paint newPaint) {
        this.newPaint = newPaint;
    }

    /**
     * @return the categoryIds
     */
    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    /**
     * @param categoryIds the categoryIds to set
     */
    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }

    /**
     * @return the tagIds
     */
    public List<Long> getTagIds() {
        return tagIds;
    }

    /**
     * @param tagIds the tagIds to set
     */
    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }
    
}
