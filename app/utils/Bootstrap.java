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
			photo = Photos.initFileToPhoto(path+"GreenTaijitu.svg", 
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
			
			//page photos
			Page p = Page.find("select p from Page p where p.title = ?", "Nacho Cheese").first();
			photo = Photos.initFileToPGEPhoto(path+"nacho.jpg", 
			    "", Photo.type.PAGE, p.id);
			p.profilePhoto = photo.id;
			p.save();
			photo = Photos.initFileToPGEPhoto(path+"nacho1.jpg", 
			    "bacon cup of nacho cheese? HELL YEAH", Photo.type.PAGE, p.id);
			photo = Photos.initFileToPGEPhoto(path+"nacho2.jpg", 
			    "", Photo.type.PAGE, p.id);
			photo = Photos.initFileToPGEPhoto(path+"nacho3.jpg", 
			    "", Photo.type.PAGE, p.id);
			photo = Photos.initFileToPGEPhoto(path+"nacho4.jpg", 
			    "thug life", Photo.type.PAGE, p.id);
			p = Page.find("select p from Page p where p.title = ?", "CATS").first();
			photo = Photos.initFileToPGEPhoto(path+"cat.jpg", 
			    "", Photo.type.PAGE, p.id);
			p.profilePhoto = photo.id;
			p.save();
			photo = Photos.initFileToPGEPhoto(path + "cat1.jpg", 
			    "",Photo.type.PAGE, p.id);
			photo = Photos.initFileToPGEPhoto(path + "cat2.jpg", 
			    "",Photo.type.PAGE, p.id);
			photo = Photos.initFileToPGEPhoto(path + "cat3.jpg", 
			    "",Photo.type.PAGE, p.id);
			photo = Photos.initFileToPGEPhoto(path + "cat4.jpg", 
			    "",Photo.type.PAGE, p.id);
			
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
