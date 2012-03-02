package unit;

import java.util.Date;

import models.User;

import org.junit.Test;

import play.test.UnitTest;

public class ModelTest extends UnitTest {

  @Test
  public void testCreatedAt() {
    // Create a new user and save it
    User bob = new User("bob@gmail.com", "secret", "Bob").save();

    assertNotNull(bob.createdAt);
  }
   
  @Test
  public void testUpdatedAt() {
    
    // Create a new user and save it
    User u = (User) User.findAll().get(0);
    
    Date updatedAtBefore = u.updatedAt;
    u.email = "foo@bar.com";
    u.save();
    
    assertFalse(u.updatedAt.equals(updatedAtBefore));
  }
  
}
