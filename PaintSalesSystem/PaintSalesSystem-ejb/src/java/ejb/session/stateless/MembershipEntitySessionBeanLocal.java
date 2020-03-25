/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Member;
import java.util.List;
import javax.ejb.Local;
import util.exception.DeleteMembershipException;
import util.exception.InputDataValidationException;
import util.exception.MembershipNotFoundException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author CHEN BINGSEN
 */
@Local
public interface MembershipEntitySessionBeanLocal {

    public void deleteMembership(Long membershipId) throws MembershipNotFoundException, DeleteMembershipException;

    public Member retrieveMembershipByMembershipId(Long membershipId) throws MembershipNotFoundException;

    public List<Member> retrieveAllMemberships();

    public Long createNewMembership(Member newMembership) throws UnknownPersistenceException, InputDataValidationException;
    
}
