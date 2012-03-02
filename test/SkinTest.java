import org.junit.*;

import controllers.Application;
import controllers.Skins;

import java.util.*;
import play.test.*;
import models.*;

public class SkinTest extends UnitTest {
  
  @Test 
  public void testSkins()
  {   
    
    // Create a new user and save it
    User bob = new User("bob@gmail.com", "secret", "Bob").save();
    
    //Bob should have a default skin
    assertNotNull(bob.skin);
    assertEquals("DEFAULT",bob.skin.name);
  }
}