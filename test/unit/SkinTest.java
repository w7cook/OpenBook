package unit;
import org.junit.*;

import controllers.Application;
import controllers.Skins;

import java.util.*;
import play.test.*;
import models.*;

public class SkinTest extends UnitTest {

  /**
   * Does unit testing on the Skins in the database, and, creating new Skins and
   * adding to the database
   */
  @Test 
  public void testDatabase()
  {   
    // Create a new user and save it
    User tester = new User("tester@gmail.com", "secret", "tester").save();
    new Profile(tester).save();

    //User tester should have a default skin
    assertNotNull(tester.profile);
    assertNotNull(tester.profile.skin);
    
    assertEquals(tester.profile.skin.skinName,"ut_skin");//default skin created
    assertEquals(tester.profile.skin.userName,"default");
  
    //All skins that are default should be public
    List <Skin> publicSkins = Skin.find("userName = ?","default").fetch();
    for(Skin s: publicSkins)
      assertEquals(s.isPublic, "true");
    
    
    
    
  }
  
}