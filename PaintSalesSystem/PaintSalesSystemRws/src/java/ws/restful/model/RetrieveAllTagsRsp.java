/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.model;

import entity.PaintTag;
import java.util.List;

/**
 *
 * @author Elgin Patt
 */
public class RetrieveAllTagsRsp {
    private List<PaintTag> tags;

    public RetrieveAllTagsRsp() {
    }

    public RetrieveAllTagsRsp(List<PaintTag> tags) {
        this.tags = tags;
    }

    /**
     * @return the tags
     */
    public List<PaintTag> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(List<PaintTag> tags) {
        this.tags = tags;
    }
    
    
}
