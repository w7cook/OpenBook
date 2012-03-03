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
    assertNotNull(tester.skin);
    assertEquals("DEFAULT",tester.skin.name);
    
    //change tester's skin to SEA (another skin in the initial-data.yml)
    boolean worked = Skins.setSkin(tester, "SEA");
    
    //make sure the Skins.setSkin had the correct output and worked correctly
    assertEquals(true,worked);
    assertNotNull(tester.skin);
    assertEquals("SEA",tester.skin.name);
    
    //try to change the skin to a skin that doesn't exist
    worked = Skins.setSkin(tester, "BADSKIN");
    assertEquals(false,worked);
    assertEquals("SEA",tester.skin.name);//the tester's skin should be unchanged
    
    //create a new Skin and set it as the tester's new skin
    //if this skin has been created correctly, then testSkin should have been 
    //added to the database of Skins and thus we should be able to change the tester's skin to testSkin
    Skin testSkin = new Skin("testSkin");
    worked = Skins.setSkin(tester, "testSkin");
    assertEquals(testSkin, tester.skin);
    
    
    
  }
}