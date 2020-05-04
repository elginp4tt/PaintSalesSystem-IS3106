/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws.restful.resources;

import com.sun.media.sound.InvalidDataException;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.DeliveryEntitySessionBeanLocal;
import ejb.session.stateless.TransactionSessionBeanLocal;
import entity.Customer;
import entity.Delivery;
import entity.DeliveryServiceTransaction;
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
import util.exception.CreateNewTransactionException;
import util.exception.CustomerNotFoundException;
import util.exception.DeliveryNotFoundException;
import util.exception.EntityInstanceExistsInCollectionException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import ws.restful.model.CreateNewDeliveryReq;
import ws.restful.model.CreateNewDeliveryRsp;
import ws.restful.model.ErrorRsp;
import ws.restful.model.RetrieveAllDeliveriesRsp;
import ws.restful.model.RetrieveDeliveryRsp;
import ws.restful.model.UpdateDeliveryReq;

/**
 * REST Web Service
 *
 * @author Elgin Patt
 */
@Path("Delivery")
public class DeliveryResource {

    TransactionSessionBeanLocal transactionSessionBean = lookupTransactionSessionBeanLocal();

    CustomerEntitySessionBeanLocal customerEntitySessionBean = lookupCustomerEntitySessionBeanLocal();

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
    public Response createNewDelivery(@QueryParam("username") String username,
            CreateNewDeliveryReq createNewDeliveryReq) {
        
        if (createNewDeliveryReq != null)
        {
            
            
                Delivery newDelivery = createNewDeliveryReq.getDelivery();
                DateTimeFormatter ft = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                LocalDateTime newDeliveryStartTime = LocalDateTime.parse(dateFormat.format(newDelivery.getDeliveryStartTime()),ft);
                LocalDateTime today = LocalDateTime.parse(dateFormat.format(new Date()),ft);
                LocalDateTime earliest = today.plusDays(2).withHour(10).withMinute(0).withSecond(0).withNano(0);

                if(newDeliveryStartTime.isBefore(earliest))
                {
                    ErrorRsp errorRsp = new ErrorRsp("The earliest time you can book is " + earliest.format(ft) + ".");
                    return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
                }
                
                if(!(newDeliveryStartTime.getHour() >= 10 && newDeliveryStartTime.getHour() <= 21 && newDeliveryStartTime.getMinute() <= 59))
                {
                    ErrorRsp errorRsp = new ErrorRsp("The selected time is not within the operation hours(10am - 10pm).");
                    return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
                }
                
                DeliveryServiceTransaction newDeliveryTransaction = new DeliveryServiceTransaction();
                newDeliveryTransaction.setItemName("Delivery Service");
                newDeliveryTransaction.setPrice(BigDecimal.valueOf(50.0));
                newDeliveryTransaction.setQuantity(BigInteger.valueOf(1l));
                
                LocalDateTime newDeliveryEndTime = newDeliveryStartTime.plusMinutes(30);//default delivery duration is 30 minutes
                Date endTime = Date.from(newDeliveryEndTime.atZone( ZoneId.systemDefault()).toInstant());
                newDelivery.setDeliveryEndTime(endTime);
                newDeliveryTransaction.setDelivery(newDelivery);
                newDelivery.setDeliveryServiceTransaction(null);
                
                
                return Response.status(Response.Status.OK).entity(new CreateNewDeliveryRsp(newDeliveryTransaction, newDeliveryTransaction.getDelivery())).build();
            
        } 
        else 
        {
            ErrorRsp errorRsp = new ErrorRsp("Invalid request");
            return Response.status(Status.BAD_REQUEST).entity(errorRsp).build();
        }
    }
    
    
    @GET
    @Path("retrieveAllDeliveries")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllDeliveries(@QueryParam("username") String username)
    {
        try
        {
            Customer customer = customerEntitySessionBean.retrieveCustomerByUsername(username);
            
            List<Delivery> allDeliveries = new ArrayList<>();
            for(Transaction t : customer.getTransactions())
            {
                for(TransactionLineItem tli: t.getTransactionLineItems())
                {
                    if(tli instanceof DeliveryServiceTransaction)
                    {
                        allDeliveries.add(((DeliveryServiceTransaction) tli).getDelivery());
                    }
                }
            }
            
            for(Delivery d: allDeliveries)
            {
                d.getDeliveryServiceTransaction().setDelivery(null);
                
                if(d.getEmployee()!=null)
                {
                    d.setEmployee(null);
                }
            }
            
            return Response.status(Status.OK).entity(new RetrieveAllDeliveriesRsp(allDeliveries)).build();
            
        }
        catch(CustomerNotFoundException ex)
        {
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
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
            
            delivery.getDeliveryServiceTransaction().setDelivery(null);
            if(delivery.getEmployee()!=null)
            {
                delivery.setEmployee(null);
            }
            
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
    
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDelivery(UpdateDeliveryReq updateDeliveryReq)
    {
        try
        {
            
            Delivery delivery = updateDeliveryReq.getDelivery();
            Delivery deliveryToUpdate = deliveryEntitySessionBean.retrieveDeliveryByDeliveryId(delivery.getDeliveryId());
            
            DateTimeFormatter ft = DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss");
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            
            LocalDateTime today = LocalDateTime.parse(dateFormat.format(new Date()),ft);
            LocalDateTime newDeliveryStartTime = LocalDateTime.parse(dateFormat.format(delivery.getDeliveryStartTime()),ft);
            LocalDateTime oldDeliveryStartTime = LocalDateTime.parse(dateFormat.format(deliveryToUpdate.getDeliveryStartTime()),ft);
            LocalDateTime oldDeliveryEndTime = LocalDateTime.parse(dateFormat.format(deliveryToUpdate.getDeliveryEndTime()),ft);
            
            
            long hourDiff = oldDeliveryStartTime.until(oldDeliveryEndTime, ChronoUnit.HOURS);
            long minuteDiff = oldDeliveryStartTime.until(oldDeliveryEndTime, ChronoUnit.MINUTES);
            
            LocalDateTime twoDayBeforeOldDeliveryStartTime = oldDeliveryStartTime.minusDays(2);
            
            if(newDeliveryStartTime.isAfter(today))
            {
                if(today.isBefore(twoDayBeforeOldDeliveryStartTime) || today.isEqual(twoDayBeforeOldDeliveryStartTime))
                {
                                        
                    if(newDeliveryStartTime.getHour() >= 10 && newDeliveryStartTime.getHour() <= 21 && newDeliveryStartTime.getMinute() <= 59)
                    {
                        
                        Date newStartTime = Date.from(newDeliveryStartTime.atZone( ZoneId.systemDefault()).toInstant());
                        
                        newDeliveryStartTime = newDeliveryStartTime.plusHours(hourDiff);
                        newDeliveryStartTime = newDeliveryStartTime.plusMinutes(minuteDiff);
                        
                        Date newEndTime = Date.from( newDeliveryStartTime.atZone( ZoneId.systemDefault()).toInstant());
                        
                        deliveryToUpdate.setLocationAddress(delivery.getLocationAddress());
                        deliveryToUpdate.setPostalCode(delivery.getPostalCode());
                        deliveryToUpdate.setDeliveryStartTime(newStartTime);
                        deliveryToUpdate.setDeliveryEndTime(newEndTime);
                        
                        deliveryEntitySessionBean.updateDelivery(deliveryToUpdate, null);
                        
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
                    ErrorRsp errorRsp = new ErrorRsp("You can only make changes with two days in advance to the actual delivery start date/time.");
                    
                    return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
                }
            }
            else
            {
                ErrorRsp errorRsp = new ErrorRsp("The new date should be at least after today.");
                
                return Response.status(Response.Status.BAD_REQUEST).entity(errorRsp).build();
            }
            
        }
        catch(DeliveryNotFoundException ex)
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
    
    
    
    

    private DeliveryEntitySessionBeanLocal lookupDeliveryEntitySessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (DeliveryEntitySessionBeanLocal) c.lookup("java:global/PaintSalesSystem/PaintSalesSystem-ejb/DeliveryEntitySessionBean!ejb.session.stateless.DeliveryEntitySessionBeanLocal");
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
