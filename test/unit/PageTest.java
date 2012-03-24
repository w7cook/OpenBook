package unit;
import org.junit.*;

import controllers.Application;
import controllers.Skins;

import java.util.*;
import play.test.*;
import models.*;

public class PageTest extends UnitTest {
	
	  /**
   * 
   *
   */
  @Test 
  public void newPageTest(){
  	User bob = new User("bob@gmail.com", "secret", "Bob").save();
  	assertNotNull(bob.createdAt);
  	Page bobsPage = new Page(bob, "testPage1", "this is a test").save();
  	assertEquals("testPage1", bobsPage.title);
  	assertEquals("this is a test", bobsPage.info);
  	assertEquals("bob@gmail.com", bobsPage.admin);
  }
  
}
