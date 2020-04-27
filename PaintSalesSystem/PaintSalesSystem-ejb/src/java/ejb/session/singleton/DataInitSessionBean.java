/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.EmployeeSessionBeanLocal;
import ejb.session.stateless.PaintCategorySessionBeanLocal;
import ejb.session.stateless.PaintSessionBeanLocal;
import ejb.session.stateless.PaintTagSessionBeanLocal;
import entity.Customer;
import entity.Delivery;
import entity.DeliveryServiceTransaction;
import entity.Employee;
import entity.MessageOfTheDay;
import entity.Paint;
import entity.PaintCategory;
import entity.PaintService;
import entity.PaintServiceTransaction;
import entity.PaintTag;
import entity.Transaction;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.AccessRightEnum;
import util.exception.EmployeeNotFoundException;

/**
 *
 * @author Elgin Patt
 */
@Singleton
@LocalBean
@Startup
public class DataInitSessionBean 
{
    @PersistenceContext(unitName = "PaintSalesSystem-ejbPU")
    private EntityManager em;
    
    @EJB
    private PaintSessionBeanLocal paintSessionBeanLocal;

    @EJB
    private PaintTagSessionBeanLocal paintTagSessionBeanLocal;

    @EJB
    private PaintCategorySessionBeanLocal paintCategorySessionBeanLocal;

    @EJB
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;
    
    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    
    public DataInitSessionBean() 
    {
    }
    
    @PostConstruct
    public void postConstruct()
    {
        try
        {
            employeeSessionBeanLocal.retrieveEmployeeById(1l);
        }
        catch(EmployeeNotFoundException ex)
        {
            initializeData();
        }
    }
    
    
    private void initializeData()
    {
        try
        {            
            Employee newEmployee1 = new Employee("manager1","password","Default","manager",AccessRightEnum.MANAGER);
            Employee newEmployee2 = new Employee("employee1","password","Default","employee",AccessRightEnum.NORMAL);
            Employee newEmployee3 = new Employee("employee2","password","Default","employee",AccessRightEnum.NORMAL);
            Customer newCustomer1 = new Customer("customer1", "customer1", "customer@gmail.com", "customer address", "customer1", "password");
            Transaction newTransaction1 = new Transaction();

            Delivery newDelivery1 = new Delivery();
            newDelivery1.setDeliveryStartTime(dateFormat.parse("10/04/2020 05:00"));
            newDelivery1.setDeliveryEndTime(dateFormat.parse("10/04/2020 05:20"));
            newDelivery1.setLocationAddress("address1");
            newDelivery1.setPostalCode("123456");
            
            DeliveryServiceTransaction newDeliveryTransaction1 = new DeliveryServiceTransaction();
            newDeliveryTransaction1.setItemName("Delivery Service");
            newDeliveryTransaction1.setPrice(BigDecimal.valueOf(50.0));
            newDeliveryTransaction1.setQuantity(BigInteger.valueOf(1l));
            
            DeliveryServiceTransaction newDeliveryTransaction2 = new DeliveryServiceTransaction();
            newDeliveryTransaction2.setItemName("Delivery Service");
            newDeliveryTransaction2.setPrice(BigDecimal.valueOf(50.0));
            newDeliveryTransaction2.setQuantity(BigInteger.valueOf(1l));
            
            Delivery newDelivery2 = new Delivery();
            newDelivery2.setDeliveryStartTime(dateFormat.parse("11/04/2020 05:00"));
            newDelivery2.setDeliveryEndTime(dateFormat.parse("11/04/2020 05:20"));
            newDelivery2.setLocationAddress("address1");
            newDelivery2.setPostalCode("123456");
            
            PaintServiceTransaction newPaintServiceTransaction1 = new PaintServiceTransaction();
            newPaintServiceTransaction1.setItemName("Paint Service");
            newPaintServiceTransaction1.setPrice(BigDecimal.valueOf(200.0));
            newPaintServiceTransaction1.setQuantity(BigInteger.valueOf(1l));
            
            PaintService newPaintService1 = new PaintService();
            newPaintService1.setPaintServiceStartTime(dateFormat.parse("15/04/2020 05:00"));
            newPaintService1.setPaintServiceEndTime(dateFormat.parse("15/04/2020 05:20"));
            newPaintService1.setLocationAddress("address1");
            newPaintService1.setPostalCode("123456");
            
            PaintServiceTransaction newPaintServiceTransaction2 = new PaintServiceTransaction();
            newPaintServiceTransaction2.setItemName("Paint Service");
            newPaintServiceTransaction2.setPrice(BigDecimal.valueOf(200.0));
            newPaintServiceTransaction2.setQuantity(BigInteger.valueOf(1l));
            
            PaintService newPaintService2 = new PaintService();
            newPaintService2.setPaintServiceStartTime(dateFormat.parse("16/04/2020 05:00"));
            newPaintService2.setPaintServiceEndTime(dateFormat.parse("16/04/2020 05:20"));
            newPaintService2.setLocationAddress("address1");
            newPaintService2.setPostalCode("123456");
            
            MessageOfTheDay messageOfTheDay1 = new MessageOfTheDay("Important", "TEXT1", dateFormat.parse("15/04/2020 05:00"));
            MessageOfTheDay messageOfTheDay2 = new MessageOfTheDay("Important", "TEXT2", dateFormat.parse("15/04/2020 05:10"));
            
            
            newCustomer1.addTransaction(newTransaction1);
            newTransaction1.addSaleTransactionLineItemEntity(newDeliveryTransaction1);
            newTransaction1.addSaleTransactionLineItemEntity(newDeliveryTransaction2);
            newTransaction1.addSaleTransactionLineItemEntity(newPaintServiceTransaction1);
            newTransaction1.addSaleTransactionLineItemEntity(newPaintServiceTransaction2);
            newDeliveryTransaction1.setDelivery(newDelivery1);
            newDeliveryTransaction2.setDelivery(newDelivery2);
            newPaintServiceTransaction1.setPaintService(newPaintService1);
            newPaintServiceTransaction2.setPaintService(newPaintService2);
            newEmployee2.addDelivery(newDelivery1);
            newEmployee2.addDelivery(newDelivery2);
            newEmployee3.addPaintService(newPaintService1);
            newEmployee3.addPaintService(newPaintService2);
            newEmployee1.addMessageOfTheDay(messageOfTheDay1);
            newEmployee1.addMessageOfTheDay(messageOfTheDay2);
            
            

            em.persist(newCustomer1);
            em.persist(newTransaction1);
            em.persist(newDeliveryTransaction1);
            em.persist(newDeliveryTransaction2);
            em.persist(newPaintServiceTransaction1);
            em.persist(newPaintServiceTransaction2);
            em.persist(newDelivery1);
            em.persist(newDelivery2);
            em.persist(newPaintService1);
            em.persist(newPaintService2);
            em.persist(newEmployee1);
            em.persist(newEmployee2);
            em.persist(newEmployee3);
            em.persist(messageOfTheDay1);
            em.persist(messageOfTheDay2);
            
            //adding paint categories
            PaintCategory pc0 = paintCategorySessionBeanLocal.createNewPaintCategory(new PaintCategory("Category 0"), null);
            PaintCategory pc1 = paintCategorySessionBeanLocal.createNewPaintCategory(new PaintCategory("Category 1"), pc0.getPaintCategoryId());
            PaintCategory pc2 = paintCategorySessionBeanLocal.createNewPaintCategory(new PaintCategory("Category 2"), pc0.getPaintCategoryId());
            PaintCategory pc3 = paintCategorySessionBeanLocal.createNewPaintCategory(new PaintCategory("Category 3"), pc0.getPaintCategoryId());
            
            //adding tags
            PaintTag hotTag = paintTagSessionBeanLocal.createNewPaintTag(new PaintTag("Hot"));
            PaintTag discountTag = paintTagSessionBeanLocal.createNewPaintTag(new PaintTag("Discount"));
            PaintTag cheapTag = paintTagSessionBeanLocal.createNewPaintTag(new PaintTag("Cheap"));

            //adding paints
            Paint paint1 = new Paint("Paint001", "P001", BigDecimal.valueOf(20.0), 100, 100, 3);
            List<Long> paintCategories = new ArrayList<>();
            List<Long> paintTags = new ArrayList<>();
            paintCategories.add(pc1.getPaintCategoryId());
            paintCategories.add(pc2.getPaintCategoryId());
            paintTags.add(cheapTag.getTagId());
            Paint paintToPersist = paintSessionBeanLocal.createNewPaint(paint1, paintCategories, paintTags);

            Paint paint2 = new Paint("Paint002", "P002", BigDecimal.valueOf(50.0), 100, 100, 5);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pc2.getPaintCategoryId());
            paintCategories.add(pc3.getPaintCategoryId());
            paintTags.add(hotTag.getTagId());
            paintTags.add(discountTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(paint2, paintCategories, paintTags);
            
            Paint paint3 = new Paint("Paint003", "P003", BigDecimal.valueOf(10.0), 100, 100, 1);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pc1.getPaintCategoryId());
            paintCategories.add(pc3.getPaintCategoryId());
            paintTags.add(cheapTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(paint3, paintCategories, paintTags);
            
            Paint paint4 = new Paint("Paint004", "P004", BigDecimal.valueOf(30.0), 100, 100, 4);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pc3.getPaintCategoryId());
            paintTags.add(discountTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(paint4, paintCategories, paintTags);

            Paint paint5 = new Paint("Paint005", "P005", BigDecimal.valueOf(20.0), 100, 100, 2);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pc1.getPaintCategoryId());
            paintCategories.add(pc3.getPaintCategoryId());            
            paintTags.add(hotTag.getTagId());
            paintTags.add(discountTag.getTagId());
            paintTags.add(cheapTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(paint5, paintCategories, paintTags);
            
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
