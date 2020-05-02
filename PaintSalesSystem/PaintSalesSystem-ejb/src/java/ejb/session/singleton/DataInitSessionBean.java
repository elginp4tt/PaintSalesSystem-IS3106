/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.CustomerEntitySessionBeanLocal;
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

public class DataInitSessionBean {

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

    private void initializeData() {
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
            
            Delivery newDelivery3 = new Delivery();
            newDelivery3.setDeliveryStartTime(dateFormat.parse("21/04/2020 05:00"));
            newDelivery3.setDeliveryEndTime(dateFormat.parse("21/04/2020 05:20"));
            newDelivery3.setLocationAddress("address3");
            newDelivery3.setPostalCode("123486");
            
            DeliveryServiceTransaction newDeliveryTransaction3 = new DeliveryServiceTransaction();
            newDeliveryTransaction3.setItemName("Delivery Service");
            newDeliveryTransaction3.setPrice(BigDecimal.valueOf(50.0));
            newDeliveryTransaction3.setQuantity(BigInteger.valueOf(1l));

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
            newTransaction1.addSaleTransactionLineItemEntity(newDeliveryTransaction3);
            newTransaction1.addSaleTransactionLineItemEntity(newPaintServiceTransaction1);
            newTransaction1.addSaleTransactionLineItemEntity(newPaintServiceTransaction2);
            newDeliveryTransaction1.setDelivery(newDelivery1);
            newDeliveryTransaction2.setDelivery(newDelivery2);
            newDeliveryTransaction3.setDelivery(newDelivery3);
            newPaintServiceTransaction1.setPaintService(newPaintService1);
            newPaintServiceTransaction2.setPaintService(newPaintService2);
            newEmployee2.addDelivery(newDelivery1);
            newEmployee2.addDelivery(newDelivery2);
            newEmployee3.addPaintService(newPaintService1);
            newEmployee3.addPaintService(newPaintService2);
            newEmployee1.addMessageOfTheDay(messageOfTheDay1);
            newEmployee1.addMessageOfTheDay(messageOfTheDay2);

            em.persist(newCustomer1);
            em.persist(newCustomer2);
            em.persist(newCustomer3);
            em.persist(newTransaction1);
            em.persist(newDeliveryTransaction1);
            em.persist(newDeliveryTransaction2);
            em.persist(newDeliveryTransaction3);
            em.persist(newPaintServiceTransaction1);
            em.persist(newPaintServiceTransaction2);
            em.persist(newDelivery1);
            em.persist(newDelivery2);
            em.persist(newDelivery3);
            em.persist(newPaintService1);
            em.persist(newPaintService2);
            em.persist(newEmployee1);
            em.persist(newEmployee2);
            em.persist(newEmployee3);
            em.persist(messageOfTheDay1);
            em.persist(messageOfTheDay2);
            
			//adding parent paint categories
            PaintCategory pcSeasons = paintCategorySessionBeanLocal.createNewPaintCategory(new PaintCategory("Seasons"), null);
            PaintCategory pcMood = paintCategorySessionBeanLocal.createNewPaintCategory(new PaintCategory("Mood"), null);
            PaintCategory pcRooms = paintCategorySessionBeanLocal.createNewPaintCategory(new PaintCategory("Rooms"), null);
			
			//adding child paint categories
            PaintCategory pcSpring = paintCategorySessionBeanLocal.createNewPaintCategory(new PaintCategory("Spring"), pcSeasons.getPaintCategoryId());
            PaintCategory pcSummer = paintCategorySessionBeanLocal.createNewPaintCategory(new PaintCategory("Summer"), pcSeasons.getPaintCategoryId());
            PaintCategory pcWinter = paintCategorySessionBeanLocal.createNewPaintCategory(new PaintCategory("Winter"), pcSeasons.getPaintCategoryId());
            PaintCategory pcAutumn = paintCategorySessionBeanLocal.createNewPaintCategory(new PaintCategory("Autumn"), pcSeasons.getPaintCategoryId());
            PaintCategory pcBright = paintCategorySessionBeanLocal.createNewPaintCategory(new PaintCategory("Bright"), pcMood.getPaintCategoryId());
            PaintCategory pcDark = paintCategorySessionBeanLocal.createNewPaintCategory(new PaintCategory("Dark"), pcMood.getPaintCategoryId());
            PaintCategory pcHappy = paintCategorySessionBeanLocal.createNewPaintCategory(new PaintCategory("Happy"), pcMood.getPaintCategoryId());
            PaintCategory pcLivingRm = paintCategorySessionBeanLocal.createNewPaintCategory(new PaintCategory("Living Room"), pcRooms.getPaintCategoryId());
            PaintCategory pcBedroom = paintCategorySessionBeanLocal.createNewPaintCategory(new PaintCategory("Bedroom"), pcRooms.getPaintCategoryId());

            //adding tags
            PaintTag hotTag = paintTagSessionBeanLocal.createNewPaintTag(new PaintTag("Hot"));
            PaintTag discountTag = paintTagSessionBeanLocal.createNewPaintTag(new PaintTag("Discount"));
            PaintTag cheapTag = paintTagSessionBeanLocal.createNewPaintTag(new PaintTag("Cheap"));
            PaintTag limitedEdTag = paintTagSessionBeanLocal.createNewPaintTag(new PaintTag("Limited Edition"));
            PaintTag seasonalTag = paintTagSessionBeanLocal.createNewPaintTag(new PaintTag("Seasonal"));
            PaintTag designerTag = paintTagSessionBeanLocal.createNewPaintTag(new PaintTag("Designer"));
            PaintTag pokemonTag = paintTagSessionBeanLocal.createNewPaintTag(new PaintTag("Pokemon"));

            //adding paints
            Paint skyBlue=new Paint("Sky Blue","#87CEEB",BigDecimal.valueOf(35.0),100,100,4);
            List<Long> paintCategories = new ArrayList<>();
            List<Long> paintTags = new ArrayList<>();
            paintCategories.add(pcSpring.getPaintCategoryId());
            paintCategories.add(pcSummer.getPaintCategoryId());
            paintCategories.add(pcBright.getPaintCategoryId());
            paintCategories.add(pcHappy.getPaintCategoryId());
            paintTags.add(cheapTag.getTagId());
            paintTags.add(hotTag.getTagId());
            Paint paintToPersist = paintSessionBeanLocal.createNewPaint(skyBlue, paintCategories, paintTags);

            Paint fuschiaPurple=new Paint("Fuschia Purple","#CC397B",BigDecimal.valueOf(30.0),100,100,2);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pcDark.getPaintCategoryId());
            paintCategories.add(pcLivingRm.getPaintCategoryId());
            paintCategories.add(pcBedroom.getPaintCategoryId());
            paintTags.add(cheapTag.getTagId());
            paintTags.add(seasonalTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(fuschiaPurple, paintCategories, paintTags);

            Paint terrificTurq=new Paint("Terrific Turquoise","#40E0D0",BigDecimal.valueOf(40.0),100,100,5);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pcWinter.getPaintCategoryId());
            paintCategories.add(pcBedroom.getPaintCategoryId());
            paintTags.add(cheapTag.getTagId());
            paintTags.add(hotTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(terrificTurq, paintCategories, paintTags);

            Paint apricotOrange=new Paint("Apricot Orange","#FBCEB1",BigDecimal.valueOf(25.0),100,100,1);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pcSpring.getPaintCategoryId());
            paintCategories.add(pcSummer.getPaintCategoryId());
            paintCategories.add(pcBright.getPaintCategoryId());
            paintCategories.add(pcHappy.getPaintCategoryId());
            paintCategories.add(pcLivingRm.getPaintCategoryId());
            paintTags.add(cheapTag.getTagId());
            paintTags.add(hotTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(apricotOrange, paintCategories, paintTags);
			
			Paint groovyGreen=new Paint("Groovy Green","#858758",BigDecimal.valueOf(35.0),100,100,4);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pcLivingRm.getPaintCategoryId());
            paintCategories.add(pcWinter.getPaintCategoryId());
            paintTags.add(cheapTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(groovyGreen, paintCategories, paintTags);
			
			Paint timber=new Paint("Timber" ,"#C19A6B",BigDecimal.valueOf(20.0),100,100,1);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pcDark.getPaintCategoryId());
            paintCategories.add(pcAutumn.getPaintCategoryId());
            paintCategories.add(pcLivingRm.getPaintCategoryId());
            paintCategories.add(pcBedroom.getPaintCategoryId());
            paintTags.add(cheapTag.getTagId());
            paintTags.add(discountTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(timber, paintCategories, paintTags);
			
			Paint bobbyBrown=new Paint("Bobby Brown","#80604D",BigDecimal.valueOf(23.0),100,100,3);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pcAutumn.getPaintCategoryId());
            paintCategories.add(pcWinter.getPaintCategoryId());
            paintCategories.add(pcDark.getPaintCategoryId());
            paintCategories.add(pcBedroom.getPaintCategoryId());
            paintTags.add(cheapTag.getTagId());
            paintTags.add(discountTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(bobbyBrown, paintCategories, paintTags);
			
			Paint bloodRed=new Paint("Blood Red","#8A0303",BigDecimal.valueOf(36.0),100,100,5);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pcAutumn.getPaintCategoryId());
            paintCategories.add(pcSummer.getPaintCategoryId());
            paintCategories.add(pcDark.getPaintCategoryId());
            paintTags.add(hotTag.getTagId());
            paintTags.add(seasonalTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(bloodRed, paintCategories, paintTags);
			
			Paint magenta=new Paint("Magenta ","#FF00FF",BigDecimal.valueOf(23.0),100,100,2);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pcDark.getPaintCategoryId());
            paintCategories.add(pcBedroom.getPaintCategoryId());
            paintCategories.add(pcWinter.getPaintCategoryId());
            paintTags.add(cheapTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(magenta, paintCategories, paintTags);
			
			Paint sunflowerYellow=new Paint("Sunflower Yellow","#E8DE2A",BigDecimal.valueOf(36.0),100,100,5);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pcSpring.getPaintCategoryId());
            paintCategories.add(pcSummer.getPaintCategoryId());
            paintCategories.add(pcHappy.getPaintCategoryId());
            paintTags.add(hotTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(sunflowerYellow, paintCategories, paintTags);
			
			Paint movingViolet=new Paint("Moving Violet","#5B0A91",BigDecimal.valueOf(35.0),100,100,3);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pcSpring.getPaintCategoryId());
            paintCategories.add(pcBright.getPaintCategoryId());
            paintTags.add(cheapTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(movingViolet, paintCategories, paintTags);
			
			Paint electricBlue=new Paint("Electric Blue","#7DF9FF",BigDecimal.valueOf(41.0),100,100,4);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pcHappy.getPaintCategoryId());
            paintCategories.add(pcBright.getPaintCategoryId());
            paintCategories.add(pcSpring.getPaintCategoryId());
            paintTags.add(seasonalTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(electricBlue, paintCategories, paintTags);
			
			Paint pikachuYellow=new Paint("Pikachu Yellow","#FBCA13",BigDecimal.valueOf(60.0),100,100,5);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pcHappy.getPaintCategoryId());
            paintCategories.add(pcBright.getPaintCategoryId());
            paintCategories.add(pcSummer.getPaintCategoryId());
            paintTags.add(pokemonTag.getTagId());
            paintTags.add(limitedEdTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(pikachuYellow, paintCategories, paintTags);
			
			Paint charmanderOrange=new Paint("Charmander Orange","#F4B185",BigDecimal.valueOf(60.0),100,100,5);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pcHappy.getPaintCategoryId());
            paintCategories.add(pcBright.getPaintCategoryId());
            paintCategories.add(pcSummer.getPaintCategoryId());
            paintTags.add(pokemonTag.getTagId());
            paintTags.add(limitedEdTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(charmanderOrange, paintCategories, paintTags);
			
			Paint bulbasaurGreen=new Paint("Bulbasaur Green","#89C893",BigDecimal.valueOf(60.0),100,100,5);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pcHappy.getPaintCategoryId());
            paintCategories.add(pcBright.getPaintCategoryId());
            paintCategories.add(pcSummer.getPaintCategoryId());
            paintTags.add(pokemonTag.getTagId());
            paintTags.add(limitedEdTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(bulbasaurGreen, paintCategories, paintTags);

            Paint myGrandfatherRoad=new Paint("My Grandfather's Road","#828181",BigDecimal.valueOf(80.0),100,100,5);
            paintCategories = new ArrayList<>();
            paintTags = new ArrayList<>();
            paintCategories.add(pcLivingRm.getPaintCategoryId());
            paintCategories.add(pcHappy.getPaintCategoryId());            
            paintTags.add(designerTag.getTagId());
            paintTags.add(limitedEdTag.getTagId());
            paintToPersist = paintSessionBeanLocal.createNewPaint(myGrandfatherRoad, paintCategories, paintTags);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
