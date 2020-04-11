/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.PaintSessionBeanLocal;
import entity.Paint;
import java.util.List;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.CreateNewPaintException;
import util.exception.InputDataValidationException;
import ws.restful.model.CreateNewPaintReq;
import ws.restful.model.CreateNewPaintRsp;
import ws.restful.model.ErrorRsp;
import util.exception.PaintExistException;
import util.exception.UnknownPersistenceException;
import ws.restful.model.RetrieveAllPaintsRsp;

/**
 * REST Web Service
 *
 * @author Elgin Patt
 */
@Path("Paint")
public class PaintResource {

    PaintSessionBeanLocal paintSessionBean = lookupPaintSessionBeanLocal();

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of PaintResource
     */
    public PaintResource() {
    }

    /**
     * Retrieves representation of an instance of
     * ws.restful.resources.PaintResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/paint")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllPaints() {
        try {
        List<Paint> paints = paintSessionBean.retrieveAllPaints();
        
        RetrieveAllPaintsRsp retrieveAllPaintsRsp = new RetrieveAllPaintsRsp(paints);
        
        return Response.status(Status.OK).entity(retrieveAllPaintsRsp).build();
        } catch (Exception ex) {
        
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    /**
     * PUT method for updating or creating an instance of PaintResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Path("/paint")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewPaint(CreateNewPaintReq createNewPaintReq) {
        if (createNewPaintReq != null) {
            try {
                Long newPaintId = paintSessionBean.createNewPaint(createNewPaintReq.getNewPaint(), createNewPaintReq.getCategoryIds(), createNewPaintReq.getTagIds()).getPaintId();

                CreateNewPaintRsp createNewPaintRsp = new CreateNewPaintRsp(newPaintId);
                
                return Response.status(Status.CREATED).entity(createNewPaintRsp).build();

            } catch (PaintExistException | UnknownPersistenceException | InputDataValidationException | CreateNewPaintException ex) {
                ErrorRsp errorRsp = new ErrorRsp("Invalid request");

                return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
            }
        } else {
            ErrorRsp errorRsp = new ErrorRsp("Invalid request");

            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
    }

    private PaintSessionBeanLocal lookupPaintSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (PaintSessionBeanLocal) c.lookup("java:global/PaintSalesSystem/PaintSalesSystem-ejb/PaintSessionBean!ejb.session.stateless.PaintSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
}
