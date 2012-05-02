package utils;
import play.*;
import play.jobs.*;
import play.test.*;
import play.libs.Crypto;

import models.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import controllers.Photos;

@OnApplicationStart
public class Bootstrap extends Job {
	
  public static Long peacePhotoID, defaultProfilePhotoID, defaultHeaderPhotoID;
  public void doJob() throws FileNotFoundException, IOException{
    // Check if the database is empty
    if(User.count() == 0) {
      Fixtures.loadModels("default-data.yml");//Default User
      //load in pictures
      Photo photo;
      String path = new java.io.File(".").getCanonicalPath() + "/public/images/";
      photo = Photos.initFileToPhoto(path+"GreenTaijitu.png",
                                     "By Chinneeb via Wikimedia Commons");
      peacePhotoID = photo.id;
      photo = Photos.initFileToPhoto(path+"default.png",
                                     "Default Profile Photo");
      defaultProfilePhotoID = photo.id;
      photo = Photos.initFileToPhoto(path + "headerBG.png",
                                     "Default Header Background Photo");
      defaultHeaderPhotoID = photo.id;
      photo = Photos.initFileToPhoto(path + "UT-Austin-Tower.jpg",
                                     "UT Austin via Wikimedia Commons");
      Fixtures.loadModels("skinTemplates.yml");//initial data for skin templates
      Fixtures.loadModels("initial-data.yml");//rest of the data
      hashPasswords();
      initFriends();
    }
  }
  
  public static void hashPasswords() {
    List<User> users= User.findAll();
    for(User u : users) {
      u.password = Crypto.passwordHash(u.password);
      u.save();
    }
  }

  public static void initFriends() {
    List<User> users = User.findAll();
    for (User u : users) {
      u.friends.add(u);
      u.save();
    }
    User bob = User.findById(2L);
    User alice = User.findById(3L);
    User jeff = User.findById(4L);

    bob.friends.add(jeff);
    alice.friends.add(jeff);
    jeff.friends.add(bob);
    jeff.friends.add(alice);
    jeff.save();
  }
}
