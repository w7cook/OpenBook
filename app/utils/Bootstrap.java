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

	public void doJob() throws FileNotFoundException, IOException{
		// Check if the database is empty
		if(User.count() == 0) {
			Fixtures.loadModels("default-data.yml");//Default User
			//load in pictures
			String path = new java.io.File(".").getCanonicalPath() + "/public/images/";
			Photos.initFileToPhoto(path+"matrixWhite.jpg", "http://media.photobucket.com/image/matrix%20white/thomastamblyn/MatrixWhite.jpg");

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
