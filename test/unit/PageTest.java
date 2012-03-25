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
  	User billy = new User("bob@gmail.com", "secret", "Billy").save();
  	assertNotNull(billy.createdAt);
  	Page billysPage = new Page(billy, "testPage1", "this is a test").save();
  	assertEquals("testPage1", billysPage.title);
  	assertEquals("this is a test", billysPage.info);
  	assertEquals(billy, billysPage.admin);
  	
  	User loser = new User("loser@gmail.com", "lame", "me").save();
  	assertNotNull(loser.createdAt);
  	Page loserPage = new Page(loser, "HappyDays", "kittens").save();
  	assertNotNull(loserPage);                                          
  }
  
  @Test
  public void newUserPageTest(){
  	User billy = User.find("select b from User b where b.username = ?", "billy").first();
   	User loser = User.find("select c from User c where c.username = ?", "loser").first();
  	Page loserPage = Page.find("select p from Page p where p.admin = ?", loser).first();
  	Page billysPage = Page.find("select p from Page p where p.admin = ?", billy).first();
  	UserPage link1 = new UserPage(billy, loserPage).save();
  	assertNotNull(link1);
  	assertEquals(link1.page, loserPage);
  	assertEquals(link1.fan, billy);
  	
  	UserPage link2 = new UserPage(billy, billysPage).save();
  	assertNotNull(link2);
  	assertEquals(link2.page, billysPage);
  	assertEquals(link2.fan, billy);
  }
  
   @Test
  public void databasePageTest(){
  	Page billysPage = Page.find("select p from Page p where p.title = ?", "testPage1").first();
  	assertNotNull(billysPage);
  }
}
