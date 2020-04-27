/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import entity.Customer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import ws.restful.model.ErrorRsp;
import util.exception.UnknownPersistenceException;
import ws.restful.model.CreateNewCustomerReq;
import ws.restful.model.CreateNewCustomerRsp;
import ws.restful.model.LoginRsp;

/**
 * REST Web Service
 *
 * @author Elgin Patt
 */
@Path("Customer")
public class CustomerResource {

    CustomerEntitySessionBeanLocal customerEntitySessionBean = lookupCustomerEntitySessionBeanLocal();


    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CustomerResource
     */
    public CustomerResource() {
    }

    /**
     * PUT method for updating or creating an instance of CustomerResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Path("/customer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewCustomer(CreateNewCustomerReq createNewCustomerReq) {
        if (createNewCustomerReq != null) {
            try {
                Long newCustomerId = customerEntitySessionBean.createNewCustomer(createNewCustomerReq.getNewCustomer());

                CreateNewCustomerRsp createNewCustomerRsp = new CreateNewCustomerRsp(newCustomerId);
                
                return Response.status(Status.CREATED).entity(createNewCustomerRsp).build();

            } catch (UnknownPersistenceException | InputDataValidationException ex) {
                ErrorRsp errorRsp = new ErrorRsp("Invalid request");

                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
            }
        } else {
            ErrorRsp errorRsp = new ErrorRsp("Invalid request");

            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
    }
    
    @Path("customerLogin")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response customerLogin(@QueryParam("username") String username, 
                                @QueryParam("password") String password)
    {
        try
        {
            Customer customer = customerEntitySessionBean.customerLogin(username, password);

            customer.setPassword(null);         
            
            return Response.status(Status.OK).entity(new LoginRsp(customer)).build();
        }
        catch(CustomerNotFoundException ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.UNAUTHORIZED).entity(errorRsp).build();
        }
        catch(Exception ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
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

}
