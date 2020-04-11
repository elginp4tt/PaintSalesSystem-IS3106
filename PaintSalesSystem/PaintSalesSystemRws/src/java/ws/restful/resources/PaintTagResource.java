/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.PaintCategorySessionBeanLocal;
import ejb.session.stateless.PaintTagSessionBeanLocal;
import entity.PaintCategory;
import entity.PaintTag;
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
import ws.restful.model.ErrorRsp;
import ws.restful.model.RetrieveAllTagsRsp;

/**
 * REST Web Service
 *
 * @author Elgin Patt
 */
@Path("Paint")
public class PaintTagResource {

    PaintTagSessionBeanLocal paintTagSessionBean = lookupPaintTagSessionBeanLocal();
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of PaintResource
     */
    public PaintTagResource() {
    }

    /**
     * Retrieves representation of an instance of
     * ws.restful.resources.PaintResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllPaintTags() {
        try {
        List<PaintTag> paintTags = paintTagSessionBean.retrieveAllTags();
        
        RetrieveAllTagsRsp retrieveAllTagsRsp = new RetrieveAllTagsRsp(paintTags);
        
        return Response.status(Status.OK).entity(retrieveAllTagsRsp).build();
        } catch (Exception ex) {
        
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

    private PaintTagSessionBeanLocal lookupPaintTagSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (PaintTagSessionBeanLocal) c.lookup("java:global/PaintSalesSystem/PaintSalesSystem-ejb/PaintTagSessionBean!ejb.session.stateless.PaintTagSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }



}
