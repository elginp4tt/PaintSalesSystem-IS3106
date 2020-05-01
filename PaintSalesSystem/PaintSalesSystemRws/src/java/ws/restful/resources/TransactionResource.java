/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.TransactionSessionBeanLocal;
import entity.Customer;
import entity.Transaction;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.CreateNewTransactionException;
import util.exception.CustomerNotFoundException;
import util.exception.CustomerTransactionNotFound;
import util.exception.InvalidLoginCredentialException;
import util.exception.TransactionNotFoundException;
import ws.restful.model.CreateNewTransactionReq;
import ws.restful.model.CreateNewTransactionRsp;
import ws.restful.model.ErrorRsp;
import ws.restful.model.RetrieveAllTransactionsRsp;
import ws.restful.model.RetrieveTransactionRsp;

/**
 * REST Web Service
 *
 * @author Elgin Patt
 */
@Path("transaction")
public class TransactionResource {

    TransactionSessionBeanLocal transactionSessionBean = lookupTransactionSessionBeanLocal();

    CustomerEntitySessionBeanLocal customerEntitySessionBean = lookupCustomerEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TransactionResource
     */
    public TransactionResource() {
    }

    @Path("retrieveAllTransactions")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransactions(@QueryParam("username") String username, 
                                @QueryParam("password") String password) {
        try
        {
            Customer customer = customerEntitySessionBean.retrieveCustomerByUsername(username);
            
            List<Transaction> transactions = transactionSessionBean.retrieveAllTransactionByUser(customer.getCustomerId());
            
            for (Transaction t : transactions){
                t.setCustomer(null);
                t.setTransactionLineItems(null);
            }
            
            return Response.status(Response.Status.OK).entity(new RetrieveAllTransactionsRsp(transactions)).build();
        }
        catch(CustomerTransactionNotFound ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Response.Status.NO_CONTENT).entity(errorRsp).build();
        }
        catch(Exception ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    @Path("retrieveTransaction/{transactionId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveTransaction(@PathParam("transactionId") Long transactionId)
    {
        try
        {
            Transaction transaction = transactionSessionBean.retrieveTransactionByTransactionId(transactionId);

            return Response.status(Status.OK).entity(new RetrieveTransactionRsp(transaction)).build();
        }
        catch(TransactionNotFoundException ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
        catch(Exception ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewTransaction(CreateNewTransactionReq createNewTransactionReq) {
        if (createNewTransactionReq != null) {
            try {
                Long newTransactionId = transactionSessionBean.createNewTransaction(createNewTransactionReq.getNewTransaction(), createNewTransactionReq.getCustomerId()).getTransactionId();

                CreateNewTransactionRsp createNewTransactionRsp = new CreateNewTransactionRsp(newTransactionId);
                
                return Response.status(Status.CREATED).entity(createNewTransactionRsp).build();

            } catch (CustomerNotFoundException | CreateNewTransactionException ex) {
                ErrorRsp errorRsp = new ErrorRsp("Invalid request");

                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
            }
        } else {
            ErrorRsp errorRsp = new ErrorRsp("Invalid request");

            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
    }

    private CustomerEntitySessionBeanLocal lookupCustomerEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (CustomerEntitySessionBeanLocal) c.lookup("java:global/PaintSalesSystem/PaintSalesSystem-ejb/CustomerEntitySessionBean!ejb.session.stateless.CustomerEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }

    private TransactionSessionBeanLocal lookupTransactionSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (TransactionSessionBeanLocal) c.lookup("java:global/PaintSalesSystem/PaintSalesSystem-ejb/TransactionSessionBean!ejb.session.stateless.TransactionSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
