/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.PaintService;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeletePaintServiceException;
import util.exception.InputDataValidationException;
import util.exception.PaintServiceNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author CHEN BINGSEN
 */
@Local
public interface PaintServiceEntitySessionBeanLocal {

    public void deletePaintService(Long paintServiceId) throws PaintServiceNotFoundException, DeletePaintServiceException;

    public void updatePaintService(PaintService paintService) throws PaintServiceNotFoundException, InputDataValidationException;

    public PaintService retrievePaintServiceByPaintServiceId(Long paintServiceId) throws PaintServiceNotFoundException;

    public List<PaintService> retrieveAllPaintService();

    public Long createNewPaintService(PaintService newPaintService) throws UnknownPersistenceException, InputDataValidationException;
    
}
