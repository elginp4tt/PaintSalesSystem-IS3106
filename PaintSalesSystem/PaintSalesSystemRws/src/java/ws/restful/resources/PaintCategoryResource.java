/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.PaintCategorySessionBeanLocal;
import entity.PaintCategory;
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
import ws.restful.model.RetrieveAllPaintCategoriesRsp;

/**
 * REST Web Service
 *
 * @author Elgin Patt
 */
@Path("Paint")
public class PaintCategoryResource {

    PaintCategorySessionBeanLocal paintCategorySessionBean = lookupPaintCategorySessionBeanLocal();

    

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of PaintResource
     */
    public PaintCategoryResource() {
    }

    /**
     * Retrieves representation of an instance of
     * ws.restful.resources.PaintResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/paintCategory")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllPaintCategories() {
        try {
        List<PaintCategory> paintCategories = paintCategorySessionBean.retrieveAllCategories();
        
        RetrieveAllPaintCategoriesRsp retrieveAllPaintCategoriesRsp = new RetrieveAllPaintCategoriesRsp(paintCategories);
        
        return Response.status(Status.OK).entity(retrieveAllPaintCategoriesRsp).build();
        } catch (Exception ex) {
        
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }


    private PaintCategorySessionBeanLocal lookupPaintCategorySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (PaintCategorySessionBeanLocal) c.lookup("java:global/PaintSalesSystem/PaintSalesSystem-ejb/PaintCategorySessionBean!ejb.session.stateless.PaintCategorySessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }


}
