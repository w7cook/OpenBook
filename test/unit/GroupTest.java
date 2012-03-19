package unit;

import org.junit.*;

import controllers.Application;

import java.util.*;
import play.test.*;
import models.*;

public class GroupTest extends UnitTest {

	@Before
	public void setup() {
		Fixtures.deleteDatabase();
	}
	
	@Test
	public void createAndRetrieveGroup() {
		// Create a new user and save it
		User bob= new User("bob@gmail.com", "secret", "Bob").save();

		// Create a new group and save it
		Group g= new Group(bob,"Bob's cool group").save();

		// Test
		assertNotNull(g);
		assertEquals("Bob's cool group", g.groupName);
		assert(bob==g.owner);
	}
	
	@Test
	public void modifyMemberList(){
		// Create a new user and save it
		User bob= new User("bob@gmail.com", "secret", "Bob").save();

		// Create a new group and save it
		Group g= new Group(bob,"Bob's cool group").save();
		
		// Test
		assert(g.getMemberCount()==1);
		
		// Add some users to the group
		User temp= new User("test1@gmail.com", "secret", "test1").save();
		g.addMember(temp);
		temp= new User("test2@gmail.com", "secret", "test2").save();
		g.addMember(temp);
		
		// Test
		assert(g.getMemberCount()==3);
		
		// Remove a member
		g.removeMember(temp);
		
		// Test
		assert(g.getMemberCount()==2);
	}
	
	@Test
	public void testGetGroups(){
		// Create three Users
		User u1= new User("bob1@gmail.com", "secret", "Bob1").save();
		User u2= new User("bob2@gmail.com", "secret", "Bob2").save();
		User u3= new User("bob3@gmail.com", "secret", "Bob3").save();
		
		// Create three Groups
		new Group(u1,"Test1").save();
		new Group(u1,"Test2").save();
		new Group(u2,"Test3").save();
		
		// Get Lists of the Groups each user is in
		List<Group> gl1= u1.getGroups();
		List<Group> gl2= u2.getGroups();
		List<Group> gl3= u3.getGroups();
		
		// Test
		assertEquals(gl1.size(),2);
		assertEquals(gl2.size(),1);
		assertEquals(gl3.size(),0);
		
		assertEquals(gl1.get(0).groupName,"Test1");
		//assertEquals(gl1.get(1).groupName,"Test2");
		assertEquals(gl2.get(0).groupName,"Test3");
		
	}
}
