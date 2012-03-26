package functional;
/**AlbumTest
 * purpose: Test all actions and usecases associated with an Album object
 * 
 * created by: Chris. Cale
 * 
 * email: collegeassignment@gmail.com
 * 
 */



import org.junit.*;
import org.junit.Before;

import controllers.AlbumController;
import controllers.Application;

import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import models.*;


public class AlbumTest extends FunctionalTest{

  User bob;
  
  @Before
  public void setup() {
    Fixtures.deleteDatabase();
    bob = new User("bob@gmail.com", "secret", "Bob").save();
//    Photo plantGIF = new Photo"/test/data/plant.gif";
//    Photo plantJPG = "/test/data/plant.jpg";
  }
  
  
  @Test
  public void testDefaultConstructor() {
//    User bob = new User("bob@gmail.com", "secret", "Bob").save();
    Album input = new Album(bob).save();
    Album output = Album.findById(input.id);
    
    assertNotNull(output);
    assertEquals(input, output);
    assertEquals(output.title, "untitled");
    assertEquals(output.owner, bob);
    assertNotNull(output.dateCreated);
    assertNotNull(output.lastDateModified);
  }
  
  
  @Test
  public void testNonDefaultConstructor() {
    User bob = new User("bob@gmail.com", "secret", "Bob").save();
    Album input = new Album("testAlbum", bob).save();
    Album output = Album.findById(input.id);
    
    assertNotNull(output);
    assertEquals(input, output);
    assertEquals(output.title, "testAlbum");
    assertEquals(output.owner, bob);
    assertNotNull(output.dateCreated);
    assertNotNull(output.lastDateModified);
  }

  
  @Test
  public void testCreate() {
    User bob = new User("bob@gmail.com", "secret", "Bob").save();
    Album newAlbum = new Album("NewAlbum", bob);
    newAlbum.save();
    //AlbumController.create("NewAlbum");
  }
  
//  @Test
//  public void testDelete(){
//    User bob = new User("bob@gmail.com", "secret", "Bob").save();
//    Album input = new Album("testAlbum", bob).save();
//    delete(input.albumId);
//  }
  



}//end of class
