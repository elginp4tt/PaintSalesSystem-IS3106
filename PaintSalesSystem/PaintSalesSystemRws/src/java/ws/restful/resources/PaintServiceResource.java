/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.PaintServiceEntitySessionBeanLocal;
import entity.PaintService;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InputDataValidationException;
import util.exception.PaintServiceNotFoundException;
import util.exception.UnknownPersistenceException;
import ws.restful.model.CreateNewPaintServiceReq;
import ws.restful.model.CreateNewPaintServiceRsp;
import ws.restful.model.ErrorRsp;
import ws.restful.model.RetrievePaintServiceRsp;

/**
 * REST Web Service
 *
 * @author Elgin Patt
 */
@Path("paintService")
public class PaintServiceResource {

    PaintServiceEntitySessionBeanLocal paintServiceEntitySessionBean = lookupPaintServiceEntitySessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of PaintServiceResource
     */
    public PaintServiceResource() {
    }

    
    @Path("retrievePaintService/{paintServiceId}")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrievePaintService(@PathParam("paintServiceId") Long paintServiceId)
    {
        try
        {
            PaintService paintService = paintServiceEntitySessionBean.retrievePaintServiceByPaintServiceId(paintServiceId);

            return Response.status(Status.OK).entity(new RetrievePaintServiceRsp(paintService)).build();
        }
        catch(PaintServiceNotFoundException ex)
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
    public Response createNewPaintService(CreateNewPaintServiceReq createNewPaintServiceReq) {
        if (createNewPaintServiceReq != null) {
            try {
                Long newPaintServiceId = paintServiceEntitySessionBean.createNewPaintService(createNewPaintServiceReq.getPaintService());

                CreateNewPaintServiceRsp createNewPaintServiceRsp = new CreateNewPaintServiceRsp(newPaintServiceId);
                
                return Response.status(Status.CREATED).entity(createNewPaintServiceRsp).build();

            } catch (UnknownPersistenceException | InputDataValidationException ex) {
                ErrorRsp errorRsp = new ErrorRsp("Invalid request");

                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
            }
        } else {
            ErrorRsp errorRsp = new ErrorRsp("Invalid request");

            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
    }

    private PaintServiceEntitySessionBeanLocal lookupPaintServiceEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (PaintServiceEntitySessionBeanLocal) c.lookup("java:global/PaintSalesSystem/PaintSalesSystem-ejb/PaintServiceEntitySessionBean!ejb.session.stateless.PaintServiceEntitySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
