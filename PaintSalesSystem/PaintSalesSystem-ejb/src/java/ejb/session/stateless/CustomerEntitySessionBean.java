package ejb.session.stateless;

import entity.Customer;
import entity.Member;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CustomerNotFoundException;
import util.exception.DeleteCustomerException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCustomerException;

/**
 *
 * @author CHEN BINGSEN
 */
@Stateless
public class CustomerEntitySessionBean implements CustomerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "PaintSalesSystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CustomerEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createNewCustomer(Customer newCustomer) throws UnknownPersistenceException, InputDataValidationException {
        try {
            Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(newCustomer);
            if (constraintViolations.isEmpty()) {
                em.persist(newCustomer);
                em.flush();

                return newCustomer.getCustomerId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            throw new UnknownPersistenceException(ex.getMessage());

        }
    }

    public Long createNewMember() {
        Customer newMember = new Member();

        return newMember.getCustomerId();
    }

    @Override
    public List<Customer> retrieveAllCustomers() {
        Query query = em.createQuery("SELECT c FROM Customer c");

        return query.getResultList();
    }

    @Override
    public Customer retrieveCustomerByCustomerId(Long customerId) throws CustomerNotFoundException {
        Customer customer = em.find(Customer.class, customerId);

        if (customer != null) {
            return customer;
        } else {
            throw new CustomerNotFoundException("Customer ID " + customerId + " does not exists!");
        }
    }

    @Override
    public Customer retrieveCustomerByUsername(String username) throws CustomerNotFoundException {

        Query query = em.createQuery("Select c FROM Customer c WHERE c.username = :inUsername");
        query.setParameter("inUsername", username);

        try {

            Customer customer = (Customer) query.getSingleResult();
            customer.getTransactions().size();
            return customer;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer Username " + username + " does not exist!");
        }
    }
    
    @Override
    public List<Customer> retrieveCustomerByCondition(String condition) {
        List<Customer> allCust = retrieveAllCustomers();
        List<Customer> sortedCust = new ArrayList<>();

        if (condition.equals("All Customer")) {
            return allCust;
        } else if (condition.equals("Normal Customer")) {
            for (Customer c : allCust) {
                if (c.getLoyaltyPoints() == null) {
                    sortedCust.add(c);
                }
            }
        } else if (condition.equals("Member Customer")) {
            for (Customer c : allCust) {
                if (c.getLoyaltyPoints() != null) {
                    sortedCust.add(c);
                }
            }

        }
        return sortedCust;
    }
    
    @Override
    public Customer makeCustomerMember (String username) throws CustomerNotFoundException{
        Customer customer = retrieveCustomerByUsername(username);
        customer.setLoyaltyPoints(BigInteger.ZERO);
        return customer;
    }

    @Override
    public Customer customerLogin(String username, String password) throws CustomerNotFoundException {

        Customer customer = retrieveCustomerByUsername(username);

        if (customer.getPassword().equals(password)) {
            return customer;
        }

        throw new CustomerNotFoundException("Customer Password does not match the account");
    }

    @Override
    public void updateCustomer(Customer customer) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException {
        if (customer != null && customer.getCustomerId() != null) {
            Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(customer);

            if (constraintViolations.isEmpty()) {
                Customer customerToUpdate = retrieveCustomerByCustomerId(customer.getCustomerId());

                if (customerToUpdate.getUsername().equals(customer.getUsername())) {
                    customerToUpdate.setFirstName(customer.getFirstName());
                    customerToUpdate.setLastName(customer.getLastName());
                    customerToUpdate.setEmail(customer.getEmail());
                    customerToUpdate.setHomeAddress(customer.getHomeAddress());
                    customerToUpdate.setPassword(customer.getPassword());
                } else {
                    throw new UpdateCustomerException("Username of customer record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CustomerNotFoundException("Customer ID not provided for customer record to be updated.");
        }
    }

    @Override
    public Customer updateCustomerForIonic(Customer customer) throws CustomerNotFoundException, UpdateCustomerException, InputDataValidationException {
        if (customer != null && customer.getCustomerId() != null) {
            Set<ConstraintViolation<Customer>> constraintViolations = validator.validate(customer);

            if (constraintViolations.isEmpty()) {
                Customer customerToUpdate = retrieveCustomerByCustomerId(customer.getCustomerId());

                if (customerToUpdate.getUsername().equals(customer.getUsername())) {
                    customerToUpdate.setFirstName(customer.getFirstName());
                    customerToUpdate.setLastName(customer.getLastName());
                    customerToUpdate.setEmail(customer.getEmail());
                    customerToUpdate.setHomeAddress(customer.getHomeAddress());
                    customerToUpdate.setPassword(customer.getPassword());
                    return customerToUpdate;
                } else {
                    throw new UpdateCustomerException("Username of customer record to be updated does not match the existing record");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } else {
            throw new CustomerNotFoundException("Customer ID not provided for customer record to be updated.");
        }
    }
    

    @Override
    public void deleteCustomer(Long customerId) throws CustomerNotFoundException, DeleteCustomerException {
        Customer customerToRemove = retrieveCustomerByCustomerId(customerId);

        if (customerToRemove.getTransactions().isEmpty()) {
            em.remove(customerToRemove);
        } else {
            throw new DeleteCustomerException("Customer Id " + customerId + " is associated with some transactions and cannot be deleted.");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<Customer>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
