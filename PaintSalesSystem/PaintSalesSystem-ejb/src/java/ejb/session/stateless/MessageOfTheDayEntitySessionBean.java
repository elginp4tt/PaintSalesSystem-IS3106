package ejb.session.stateless;

import entity.Employee;
import entity.MessageOfTheDay;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;



@Stateless
@Local(MessageOfTheDayEntitySessionBeanLocal.class)

public class MessageOfTheDayEntitySessionBean implements MessageOfTheDayEntitySessionBeanLocal
{
    @PersistenceContext(unitName = "PaintSalesSystem-ejbPU")
    private EntityManager entityManager;
    
    
    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    
    
    public MessageOfTheDayEntitySessionBean() 
    {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    
    @Override
    public MessageOfTheDay createNewMessageOfTheDay(MessageOfTheDay newMessageOfTheDay) throws InputDataValidationException
    {
        Set<ConstraintViolation<MessageOfTheDay>>constraintViolations = validator.validate(newMessageOfTheDay);
        
        if(constraintViolations.isEmpty())
        {
            entityManager.persist(newMessageOfTheDay);
            entityManager.flush();
            
            return newMessageOfTheDay;
        }
        else
        {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    
    
    @Override
    public List<MessageOfTheDay> retrieveAllMessagesOfTheDay()
    {
        Query query = entityManager.createQuery("SELECT motd FROM MessageOfTheDay motd ORDER BY motd.motdId ASC");
        
        return query.getResultList();
    }
    
    
    
    @Override
    public MessageOfTheDay removeMotd(Long motdId, Long employeeId)
    {
        MessageOfTheDay motd = entityManager.find(MessageOfTheDay.class, motdId);
        Employee employee = entityManager.find(Employee.class, employeeId);
        employee.removeMessageOfTheDay(motd);
        entityManager.merge(employee);
        entityManager.remove(motd);
        return motd;
    }
    
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<MessageOfTheDay>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
