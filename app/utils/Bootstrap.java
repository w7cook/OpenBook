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
	public static Long peacePhotoID, defaultProfilePhotoID;
	public void doJob() throws FileNotFoundException, IOException{
		// Check if the database is empty
		if(User.count() == 0) {
			Fixtures.loadModels("default-data.yml");//Default User
			//load in pictures
			Photo photo;
			String path = new java.io.File(".").getCanonicalPath() + "/public/images/";
			photo = Photos.initFileToPhoto(path+"GreenTaijitu.svg", "By Chinneeb (Own work) [Public domain], via Wikimedia Commons");
			peacePhotoID = photo.id;
			photo = Photos.initFileToPhoto(path+"default.png", "Default Profile Photo");
			defaultProfilePhotoID = photo.id;
			Fixtures.loadModels("skinTemplates.yml");//initial data for skin templates
			Fixtures.loadModels("initial-data.yml");//rest of the data
			hashPasswords();
		}
	}

  public static void hashPasswords() {
    List<User> users= User.findAll();
    for(User u : users) {
    	u.password = Crypto.passwordHash(u.password);
    	u.save();
    }
  }
}
