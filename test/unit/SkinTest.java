package unit;
import org.junit.*;

import controllers.Application;
import controllers.Skins;

import java.util.*;
import play.test.*;
import models.*;

public class SkinTest extends UnitTest {
  
  /**
   * Does unit testing on the Skins in the database, setSkin, and, creating new Skins and
   * adding to the database
   */
  @Test 
  public void testDatabase()
  {   
    // Create a new user and save it
    User tester = new User("tester@gmail.com", "secret", "tester").save();
    
    
    //User tester should have a default skin
    assertNotNull(tester.profile);
    assertNotNull(tester.profile.skin);
    
    //create a new Skin and set it as the tester's new skin
    //if this skin has been created correctly, then testSkin should have been 
    //added to the database of Skins and thus we should be able to change the tester's skin to testSkin
    Skin testSkin = new Skin("testSkin");
    assertTrue(Skins.setSkin(tester.profile, "testSkin"));
    assertEquals(testSkin, tester.profile.skin);
    
    
    
  }
  
}