/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.DeliveryEntitySessionBeanLocal;
import entity.Delivery;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.DeliveryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import ws.restful.model.CreateNewDeliveryReq;
import ws.restful.model.CreateNewDeliveryRsp;
import ws.restful.model.ErrorRsp;
import ws.restful.model.RetrieveDeliveryRsp;

/**
 * REST Web Service
 *
 * @author Elgin Patt
 */
@Path("delivery")
public class DeliveryResource {

    DeliveryEntitySessionBeanLocal deliveryEntitySessionBean = lookupDeliveryEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of DeliveryResource
     */
    public DeliveryResource() {
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewDelivery(CreateNewDeliveryReq createNewDeliveryReq) {
        if (createNewDeliveryReq != null) {
            try {
                Long newDeliveryId = deliveryEntitySessionBean.createNewDelivery(createNewDeliveryReq.getNewDelivery());

                CreateNewDeliveryRsp createNewDeliveryRsp = new CreateNewDeliveryRsp(newDeliveryId);
                
                return Response.status(Status.CREATED).entity(createNewDeliveryRsp).build();

            } catch (UnknownPersistenceException | InputDataValidationException ex) {
                ErrorRsp errorRsp = new ErrorRsp("Invalid request");

                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
            }
        } else {
            ErrorRsp errorRsp = new ErrorRsp("Invalid request");

            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
    }
    
    @Path("retrieveDelivery/{deliveryId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveDelivery(@PathParam("deliveryId") Long deliveryId)
    {
        try
        {
            Delivery delivery = deliveryEntitySessionBean.retrieveDeliveryByDeliveryId(deliveryId);

            return Response.status(Status.OK).entity(new RetrieveDeliveryRsp(delivery)).build();
        }
        catch(DeliveryNotFoundException ex)
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

    private DeliveryEntitySessionBeanLocal lookupDeliveryEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (DeliveryEntitySessionBeanLocal) c.lookup("java:global/PaintSalesSystem/PaintSalesSystem-ejb/DeliveryEntitySessionBean!ejb.session.stateless.DeliveryEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
