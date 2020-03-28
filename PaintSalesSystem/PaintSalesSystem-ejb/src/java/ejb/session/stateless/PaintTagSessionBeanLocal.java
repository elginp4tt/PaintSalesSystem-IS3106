/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PaintTag;
import java.util.List;
import javax.ejb.Local;
import util.exception.CreateNewTagException;
import util.exception.DeleteTagException;
import util.exception.InputDataValidationException;
import util.exception.TagNotFoundException;
import util.exception.UpdateTagException;

/**
 *
 * @author matto
 */
@Local
public interface PaintTagSessionBeanLocal {

    public void deleteTag(Long tagId) throws TagNotFoundException, DeleteTagException;

    public PaintTag createNewPaintTag(PaintTag newPaintTag) throws InputDataValidationException, CreateNewTagException;

    public List<PaintTag> retrieveAllTags();

    public PaintTag retrieveTagByTagId(Long tagId) throws TagNotFoundException;

    public void updateTag(PaintTag paintTag) throws InputDataValidationException, TagNotFoundException, UpdateTagException;
    
}
