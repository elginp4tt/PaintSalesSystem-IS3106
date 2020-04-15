/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Delivery;
import entity.Employee;
<<<<<<< HEAD

=======
import entity.PaintService;
>>>>>>> cfa6c531635e14e8dad29e7256d29ddb482d819c
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteEmployeeException;
import util.exception.DeliveryNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeeUsernameExistException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PaintServiceNotFoundException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateEmployeeException;

/**
 *
 * @author user
 */
@Local
public interface EmployeeSessionBeanLocal {

    public Employee createNewEmployee(Employee newEmployee) throws InputDataValidationException, UnknownPersistenceException, EmployeeUsernameExistException;

    public List<Employee> retrieveAllEmployee();

    public void updateEmployee(Employee employee, List<Long> deliveryIds, List<Long> paintServiceIds) throws EmployeeNotFoundException, DeliveryNotFoundException, PaintServiceNotFoundException, UpdateEmployeeException, InputDataValidationException;

    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException, DeleteEmployeeException;

    public Employee retrieveEmployeeById(Long employeeId) throws EmployeeNotFoundException;
<<<<<<< HEAD
    
    public List<Employee> retrieveAvailableEmployeeByDate(Date startTime, Date endTime);
=======

    public boolean checkOverlap(Date startA, Date endA, Date startB, Date endB);

    public List<Employee> retrieveAvailableEmployee(Date startTime, Date endTime, Long deliveryId, Long paintServicId);
>>>>>>> cfa6c531635e14e8dad29e7256d29ddb482d819c

    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException;

    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException;

    public Employee updateEmployeePassword(String username, String oldPassword, String newPassword) throws EmployeeNotFoundException, UpdateEmployeeException, InputDataValidationException;

}
