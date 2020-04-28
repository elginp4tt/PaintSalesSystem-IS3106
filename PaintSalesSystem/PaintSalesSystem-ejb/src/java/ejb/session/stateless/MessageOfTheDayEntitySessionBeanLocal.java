/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.MessageOfTheDay;
import java.util.List;
import javax.ejb.Local;
import util.exception.InputDataValidationException;

/**
 *
 * @author CHEN BINGSEN
 */
@Local
public interface MessageOfTheDayEntitySessionBeanLocal {

    public MessageOfTheDay createNewMessageOfTheDay(MessageOfTheDay newMessageOfTheDay) throws InputDataValidationException;

    public List<MessageOfTheDay> retrieveAllMessagesOfTheDay();

    public MessageOfTheDay removeMotd(Long motdId, Long employeeId);
    
}
