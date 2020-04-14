/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Paint;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNotFoundException;
import util.exception.CreateNewPaintException;
import util.exception.DeletePaintException;
import util.exception.InputDataValidationException;
import util.exception.PaintExistException;
import util.exception.PaintNotFoundException;
import util.exception.TagNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdatePaintException;

/**
 *
 * @author matto
 */
@Local
public interface PaintSessionBeanLocal {

    public List<Paint> retrieveAllPaints();

    public List<Paint> searchPaintsByName(String searchString);

    public List<Paint> filterPaintsByTags(List<Long> tagIds, String condition);

    public Paint retrievePaintByPaintId(Long paintId) throws PaintNotFoundException;

    public Paint retrievePaintByPaintColourCode(String colourCode) throws PaintNotFoundException;

    public void updatePaint(Paint paint, List<Long> categoryIds, List<Long> tagIds) throws PaintNotFoundException, CategoryNotFoundException, TagNotFoundException, UpdatePaintException, InputDataValidationException;

    public Paint createNewPaint(Paint newPaint, List<Long> categoryIds, List<Long> tagIds) throws PaintExistException, UnknownPersistenceException, InputDataValidationException, CreateNewPaintException;

    public List<Paint> filterPaintsByCategories(List<Long> categoryIds, String condition);

    public void deletePaint(Long paintId) throws PaintNotFoundException, DeletePaintException;
    
}
