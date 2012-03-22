package unit;

import org.junit.*;

import controllers.Application;

import java.util.*;
import play.test.*;
import models.*;

public class EventTest extends UnitTest {
  

  
@Test
  public void testEvents() {
    //Create new user and save it
    User bob = new User("bob@gmail.com", "secret", "Bob").save();
    
    //Create new event
    Event bobEvent = new Event(bob, "Bob's Birthday Party", "There might be cake", "Bob's House").save();
    Event anotherBobEvent = new Event(bob, "Board Game Night", "There might be board games", "Bob's Girlfriend's House").save();
    
    //Assert there are two events
    assertEquals(2, Event.count());
    
    //Create new user guest and save it
    User jeff = new User("jeff@gmail.com", "secret", "Jeff").save();
    bobEvent.newEventInvite(jeff);
    
    //Assert number of users and eventInvites
    
    assertEquals(1, EventInvite.count());
    
    //Delete an event
    anotherBobEvent.delete();
    assertEquals(1, Event.count());
  }


}