package unit;

import org.junit.*;

import controllers.Application;

import java.util.*;
import play.test.*;
import models.*;

public class NoteTest extends UnitTest {
  
/*
 * Author: Andy Eskridge
 * email: andy.eskridge@gmail.com
 */
  
@Test
  public void testNotes() {
    //Create new user and save it
    User bob = new User("bob@gmail.com", "secret", "Bob").save();
    
    //Create new note
    Note n = new Note(bob, "This is a title", "This is content");
    n.save();
    
    
    //Assert there is one note
    assertTrue(Note.count() == 1);
    //Delete the note
    n.delete();
    
    //Assert there are none
    assertTrue(Note.count() == 0);
    
  }


}