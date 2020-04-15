/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Employee;
import entity.PaintService;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeletePaintServiceException;
import util.exception.EmployeeNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.PaintServiceNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdatePaintServiceException;

/**
 *
 * @author CHEN BINGSEN
 */
@Local
public interface PaintServiceEntitySessionBeanLocal {

    public void deletePaintService(Long paintServiceId) throws PaintServiceNotFoundException, DeletePaintServiceException;

    public PaintService retrievePaintServiceByPaintServiceId(Long paintServiceId) throws PaintServiceNotFoundException;

    public List<PaintService> retrieveAllPaintService();

    public Long createNewPaintService(PaintService newPaintService) throws UnknownPersistenceException, InputDataValidationException;

    public List<PaintService> retrievePaintServiceByYear(String year);

    public List<PaintService> retrievePaintServiceByDate(Date date);

    public List<PaintService> retrieveDeliveryByDates(Date startDate, Date endDate);

    public List<Employee> retrieveAvailableEmployeeByNewPaintServiceDate(PaintService paintService, Date newStartDate, Date newEndDate) throws PaintServiceNotFoundException;

    public void checkAssignedEmployeeAvailability(Date newStartTime, Date newEndTime, Long paintServiceId, Long assignedEmployeeId) throws UpdatePaintServiceException, PaintServiceNotFoundException;

    public void updatePaintService(PaintService paintService, Long employeeId) throws PaintServiceNotFoundException, InputDataValidationException, EmployeeNotFoundException;
    
}
