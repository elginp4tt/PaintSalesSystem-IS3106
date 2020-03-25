/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Membership;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.DeleteMembershipException;
import util.exception.InputDataValidationException;
import util.exception.MembershipNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author CHEN BINGSEN
 */
@Stateless
public class MembershipEntitySessionBean implements MembershipEntitySessionBeanLocal {

    @PersistenceContext(unitName = "PaintSalesSystem-ejbPU")
    private EntityManager em;

    
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public MembershipEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    @Override
    public Long createNewMembership(Membership newMembership) throws UnknownPersistenceException,InputDataValidationException
    {
        try
        {
            Set<ConstraintViolation<Membership>> constraintViolations = validator.validate(newMembership);
            if(constraintViolations.isEmpty())
            {
                em.persist(newMembership);
                em.flush();
                
                return newMembership.getCustomerId();
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        }
        catch(PersistenceException ex)
        {
            throw new UnknownPersistenceException(ex.getMessage());
            
        }
        
    }
    
    
    @Override
    public List<Membership> retrieveAllMemberships()
    {
        Query query = em.createQuery("SELECT m FROM Membership m");
        return query.getResultList();
    }
    
    
    @Override
    public Membership retrieveMembershipByMembershipId(Long membershipId) throws MembershipNotFoundException
    {
        Membership membership = em.find(Membership.class,membershipId);
        
        if(membership != null)
        {
            return membership;
        }
        else
        {
            throw new MembershipNotFoundException("Membership ID " + membershipId + " does not exists!");
        }
    }
    
    
    //update function is ignored here since session bean can just manipulate the only attribute in membership locally within the persistence context
    
    
    
    @Override
    public void deleteMembership(Long membershipId) throws MembershipNotFoundException, DeleteMembershipException
    {
        Membership membershipToRemove = retrieveMembershipByMembershipId(membershipId);
        
        if(membershipToRemove.getTransactions().isEmpty())
        {
            em.remove(membershipToRemove);
        }
        else
        {
            throw new DeleteMembershipException("Customer Id " + membershipId + " is associated with some transactions and cannot be deleted.");
        }
    }
    
    
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Membership>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    } 
}
