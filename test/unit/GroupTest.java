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
		Group g= new Group(bob,"Bob's cool group","A description").save();

		// Test
		assertNotNull(g);
		assertEquals("Bob's cool group", g.groupName);
		assert(bob==g.owner);
	}
	
	@Test
	public void modifyMemberList(){
		// Create a new user and save it
		User bob= new User("bob@gmail.com", "secret", "Bob").save();
		User u2= new User("test@test.com","secret", "Fred").save();

		// Create a new group and save it
		Group g= new Group(bob,"Bob's cool group","A description").save();
		Group g2= new Group(u2,"Group2","g2").save();
		
		// Test
		assert(g.getMemberCount()==1);
		assert(g2.getMemberCount()==1);
		
		// Add some users to the group
		User temp= new User("test1@gmail.com", "secret", "test1").save();
		g.addMember(temp);
		temp= new User("test2@gmail.com", "secret", "test2").save();
		g.addMember(temp);
		
		// Test
		assert(g.getMemberCount()==3);
		assert(g2.getMemberCount()==1);
		
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
		new Group(u1,"Test1","A description").save();
		//new Group(u1,"Test2").save();// Primary Key Problem?
		new Group(u2,"Test3","A description").save();
		
		// Get Lists of the Groups each user is in
		List<Group> gl1= u1.getGroups();
		List<Group> gl2= u2.getGroups();
		List<Group> gl3= u3.getGroups();
		
		// Test (A user can't own more than one group...)
		//assertEquals(gl1.size(),2);
		assertEquals(gl2.size(),1);
		assertEquals(gl3.size(),0);
		
		assertEquals(gl1.get(0).groupName,"Test1");
		//assertEquals(gl1.get(1).groupName,"Test2");
		assertEquals(gl2.get(0).groupName,"Test3");
		
	}
	
	@Test
	public void testGroupPosts(){
		//Create a User
		User u1= new User("test@gmail.com","secret", "T1").save();
		
		//Create a Group
		Group g1= new Group(u1,"Test1","Group with posts").save();
		
		//Create a Post (but don't save it in the user table)
		Post p1= new Post(g1, u1, "Content");
//		assertEquals(g1.groupPosts.size(),0);
//		g1. addPost(p1);
//		List posts= g1.getPosts();
//		
//		//Test
//		assertEquals(g1.getPosts().size(),1);
//		assertEquals(posts.size(),1);
		
		
	}
}
