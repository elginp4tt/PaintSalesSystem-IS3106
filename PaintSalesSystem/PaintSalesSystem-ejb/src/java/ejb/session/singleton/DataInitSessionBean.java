/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.EmployeeSessionBeanLocal;
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
public class DataInitSessionBean {

    @EJB(name = "EmployeeSessionBeanLocal")
    private EmployeeSessionBeanLocal employeeSessionBeanLocal;

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    
    @PersistenceContext(unitName = "PaintSalesSystem-ejbPU")
    private EntityManager em;

    private static final DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public DataInitSessionBean() {
    }

    @PostConstruct
    public void postConstruct() {
        try {
            employeeSessionBeanLocal.retrieveEmployeeById(1l);
        } catch (EmployeeNotFoundException ex) {
            initializeData();
        }
    }

    public void initializeData() {
        try {

            Employee newEmployee1 = new Employee("manager1", "password", "Default", "manager", AccessRightEnum.MANAGER);
            Employee newEmployee2 = new Employee("employee1", "password", "Default", "employee", AccessRightEnum.NORMAL);
            Employee newEmployee3 = new Employee("employee2", "password", "Default", "employee", AccessRightEnum.NORMAL);
            Customer newCustomer1 = new Customer("customer1", "customer1", "customer@gmail.com", "customer address", "customer1", "password");
            Customer newCustomer2 = new Customer("customer2", "customer2", "customer2@gmail.com", "customer 2 address", "customer2", "password");
            Customer newCustomer3 = new Customer("customer3", "customer3", "customer3@gmail.com", "customer 3 address", "customer3", "password");
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
            
            //adding paint categories
            PaintCategory pc1 = new PaintCategory("Category 1");
            PaintCategory pc2 = new PaintCategory("Category 2");
            PaintCategory pc3 = new PaintCategory("Category 3");
            em.persist(pc1);
            em.persist(pc2);
            em.persist(pc3);
            
            //adding tags
            PaintTag hotTag = new PaintTag("Hot");
            PaintTag discountTag = new PaintTag("Discount");
            PaintTag cheapTag = new PaintTag("Cheap");
            em.persist(hotTag);
            em.persist(discountTag);
            em.persist(cheapTag);
            
            //adding paints
            Paint paint1 = new Paint("Paint001", "P001", BigDecimal.valueOf(20.0), 100, 100, 3);
            paint1.getPaintCategories().add(pc1);
            pc1.getPaints().add(paint1);
            paint1.getPaintCategories().add(pc2);
            pc2.getPaints().add(paint1);
            paint1.getTags().add(cheapTag);
            cheapTag.getPaints().add(paint1);
            em.persist(paint1);
            em.flush();
            
            Paint paint2 = new Paint("Paint002", "P002", BigDecimal.valueOf(50.0), 100, 100, 5);
            paint2.getPaintCategories().add(pc2);
            pc2.getPaints().add(paint2);
            paint2.getPaintCategories().add(pc3);
            pc3.getPaints().add(paint2);
            paint2.getTags().add(hotTag);
            hotTag.getPaints().add(paint2);
            paint2.getTags().add(discountTag);
            discountTag.getPaints().add(paint2);
            em.persist(paint2);
            em.flush();
            
            Paint paint3 = new Paint("Paint003", "P003", BigDecimal.valueOf(10.0), 100, 100, 1);
            paint3.getPaintCategories().add(pc1);
            pc1.getPaints().add(paint3);
            paint3.getPaintCategories().add(pc3);
            pc3.getPaints().add(paint3);
            paint3.getTags().add(discountTag);
            discountTag.getPaints().add(paint3);
            em.persist(paint3);
            em.flush();
            
            Paint paint4 = new Paint("Paint004", "P004", BigDecimal.valueOf(30.0), 100, 100, 4);
            paint4.getPaintCategories().add(pc3);
            pc3.getPaints().add(paint4);
            paint4.getTags().add(discountTag);
            discountTag.getPaints().add(paint4);
            em.persist(paint4);
            em.flush();

            Paint paint5 = new Paint("Paint005", "P005", BigDecimal.valueOf(20.0), 100, 100, 2);
            paint5.getPaintCategories().add(pc1);
            pc1.getPaints().add(paint5);
            paint5.getPaintCategories().add(pc2);
            pc2.getPaints().add(paint5);
            paint5.getPaintCategories().add(pc3);
            pc3.getPaints().add(paint5);
            paint5.getTags().add(hotTag);
            hotTag.getPaints().add(paint5);
            paint5.getTags().add(discountTag);
            discountTag.getPaints().add(paint5);
            paint5.getTags().add(cheapTag);
            cheapTag.getPaints().add(paint5);
            em.persist(paint5);
            em.flush();

            em.persist(newCustomer1);
            em.persist(newCustomer2);
            em.persist(newCustomer3);
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

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
