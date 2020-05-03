/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.PaintServiceEntitySessionBeanLocal;
import ejb.session.stateless.TransactionSessionBeanLocal;
import entity.Customer;
import entity.DeliveryServiceTransaction;
import entity.PaintService;
import entity.PaintServiceTransaction;
import entity.Transaction;
import entity.TransactionLineItem;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.PaintServiceNotFoundException;
import util.exception.UnknownPersistenceException;
import ws.restful.model.CreateNewPaintServiceReq;
import ws.restful.model.CreateNewPaintServiceRsp;
import ws.restful.model.ErrorRsp;
import ws.restful.model.RetrieveAllPaintServicesRsp;
import ws.restful.model.RetrievePaintServiceRsp;
import ws.restful.model.UpdatePaintServiceReq;

/**
 * REST Web Service
 *
 * @author Elgin Patt
 */
@Path("PaintService")
public class PaintServiceResource {

    TransactionSessionBeanLocal transactionSessionBean = lookupTransactionSessionBeanLocal();

    CustomerEntitySessionBeanLocal customerEntitySessionBean = lookupCustomerEntitySessionBeanLocal();

    PaintServiceEntitySessionBeanLocal paintServiceEntitySessionBean = lookupPaintServiceEntitySessionBeanLocal();

    
    
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of PaintServiceResource
     */
    public PaintServiceResource() {
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewPaintService(@QueryParam("username") String username,
            CreateNewPaintServiceReq createNewPaintServiceReq) 
    {
        
        if (createNewPaintServiceReq != null) 
        {
            try 
            {
                PaintService newPaintService = createNewPaintServiceReq.getPaintService();
                DateTimeFormatter ft = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                LocalDateTime newPaintServiceStartTime = LocalDateTime.parse(dateFormat.format(newPaintService.getPaintServiceStartTime()),ft);
                LocalDateTime today = LocalDateTime.parse(dateFormat.format(new Date()),ft);
                LocalDateTime earliest = today.plusDays(2).withHour(10).withMinute(0).withSecond(0).withNano(0);
                
                if(newPaintServiceStartTime.isBefore(earliest))
                {
                    ErrorRsp errorRsp = new ErrorRsp("The earliest time you can book is " + earliest.format(ft) + ".");
                    return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
                }
                
                if(!(newPaintServiceStartTime.getHour() >= 10 && newPaintServiceStartTime.getHour() <= 21 && newPaintServiceStartTime.getMinute() <= 59))
                {
                    ErrorRsp errorRsp = new ErrorRsp("The selected time is not within the operation hours(10am - 10pm).");
                    return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
                }
                
                Customer customer = customerEntitySessionBean.retrieveCustomerByUsername(username);
                Transaction newTransaction = new Transaction();
                
                PaintServiceTransaction newPaintServiceTransaction = new PaintServiceTransaction();
                newPaintServiceTransaction.setItemName("Paint Service");
                newPaintServiceTransaction.setPrice(BigDecimal.valueOf(200.0));
                newPaintServiceTransaction.setQuantity(BigInteger.valueOf(1l));
                newTransaction.addSaleTransactionLineItemEntity(newPaintServiceTransaction);
                
                LocalDateTime newPaintServiceEndTime = newPaintServiceStartTime.plusDays(3);//default delivery duration is 3 days
                Date endTime = Date.from(newPaintServiceEndTime.atZone( ZoneId.systemDefault()).toInstant());
                newPaintService.setPaintServiceEndTime(endTime);
                newPaintServiceTransaction.setPaintService(newPaintService);
                
//                transactionSessionBean.createNewTransaction(newTransaction, customer.getCustomerId());
                return Response.status(Response.Status.OK).build();
            }
            catch(CustomerNotFoundException ex)
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
        else 
        {
            ErrorRsp errorRsp = new ErrorRsp("Invalid request");
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
    }
    

    @GET
    @Path("retrieveAllPaintServices")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllPaintServices(@QueryParam("username") String username)
    {
        try
        {
            Customer customer = customerEntitySessionBean.retrieveCustomerByUsername(username);
            
            List<PaintService> allPaintServices = new ArrayList<>();
            for(Transaction t : customer.getTransactions())
            {
                for(TransactionLineItem tli: t.getTransactionLineItems())
                {
                    if(tli instanceof PaintServiceTransaction)
                    {
                        allPaintServices.add(((PaintServiceTransaction) tli).getPaintService());
                    }
                }
            }
            
            for(PaintService ps : allPaintServices)
            {
                ps.getPaintServiceTransaction().setPaintService(null);
                
                if(ps.getEmployee() != null)
                {
                    ps.setEmployee(null);
                }
            }
            
            return Response.status(Status.OK).entity(new RetrieveAllPaintServicesRsp(allPaintServices)).build();
        }
        catch(CustomerNotFoundException ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }
    
    
    
    @GET
    @Path("retrievePaintService/{paintServiceId}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrievePaintService(@PathParam("paintServiceId") Long paintServiceId)
    {
        try
        {
            PaintService paintService = paintServiceEntitySessionBean.retrievePaintServiceByPaintServiceId(paintServiceId);

            paintService.getPaintServiceTransaction().setPaintService(null);
            if(paintService.getEmployee() != null)
            {
                paintService.setEmployee(null);
            }
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePaintService(UpdatePaintServiceReq updatePaintServiceReq)
    {
        try
        {
            PaintService paintService = updatePaintServiceReq.getPaintService();
            PaintService paintServiceToUpdate = paintServiceEntitySessionBean.retrievePaintServiceByPaintServiceId(paintService.getPaintServiceId());
        
            DateTimeFormatter ft = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            
            LocalDateTime today = LocalDateTime.parse(dateFormat.format(new Date()),ft);
            LocalDateTime newPaintServiceStartTime = LocalDateTime.parse(dateFormat.format(paintService.getPaintServiceStartTime()),ft);
            LocalDateTime oldPaintServiceStartTime = LocalDateTime.parse(dateFormat.format(paintServiceToUpdate.getPaintServiceStartTime()),ft);
            LocalDateTime oldPaintServiceEndTime = LocalDateTime.parse(dateFormat.format(paintServiceToUpdate.getPaintServiceEndTime()),ft);
            
            long hourDiff = oldPaintServiceStartTime.until(oldPaintServiceEndTime, ChronoUnit.HOURS);
            long minuteDiff = oldPaintServiceStartTime.until(oldPaintServiceEndTime, ChronoUnit.MINUTES);
            
            LocalDateTime twoDayBeforeOldPaintServiceStartTime = oldPaintServiceStartTime.minusDays(2);
            
            if(newPaintServiceStartTime.isAfter(today))
            {
                if(today.isBefore(twoDayBeforeOldPaintServiceStartTime) || today.isEqual(twoDayBeforeOldPaintServiceStartTime))
                {
                    if(newPaintServiceStartTime.getHour() >= 10 && newPaintServiceStartTime.getHour() <= 21 && newPaintServiceStartTime.getMinute() <= 59)
                    {
                        Date newStartTime = Date.from(newPaintServiceStartTime.atZone( ZoneId.systemDefault()).toInstant());
                        
                        newPaintServiceStartTime = newPaintServiceStartTime.plusHours(hourDiff);
                        newPaintServiceStartTime = newPaintServiceStartTime.plusMinutes(minuteDiff);
                        
                        Date newEndTime = Date.from( newPaintServiceStartTime.atZone( ZoneId.systemDefault()).toInstant());
                        
                        paintServiceToUpdate.setLocationAddress(paintService.getLocationAddress());
                        paintServiceToUpdate.setPostalCode(paintService.getPostalCode());
                        paintServiceToUpdate.setPaintServiceStartTime(newStartTime);
                        paintServiceToUpdate.setPaintServiceEndTime(newEndTime);
                        
                        paintServiceEntitySessionBean.updatePaintService(paintServiceToUpdate, null);
                        
                        return Response.status(Response.Status.OK).build();
                    }
                    else
                    {
                        ErrorRsp errorRsp = new ErrorRsp("The selected time is not within the operation hours(10am - 10pm).");
                        return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
                    }
                }
                else
                {
                    ErrorRsp errorRsp = new ErrorRsp("You can only make changes with two days in advance to the actual paint service start date/time.");
                    return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
                }
            }
            else
            {
                ErrorRsp errorRsp = new ErrorRsp("The new date should be at least after today.");
                return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
            }
        }
        catch(PaintServiceNotFoundException ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
        }
        catch(Exception ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
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
