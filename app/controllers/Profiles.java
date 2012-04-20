package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Profiles extends OBController {
	
	public static void updateBio(String bio){
		User user  = user();
		Profile profile = Profile.find("owner = ?", user).first();
		profile.bio = bio;
		profile.save();
		renderTemplate("Application/edit_basic.html", profile);
	}
	
	public static void updateInformation(Date birthday, String relationshipStatus, String gender, String interestedIn, 
	Date anniversary, String language, String religion, String political){
		User user = user();
		Profile profile = Profile.find("owner = ?", user).first();
	    profile.religion = religion;
	    profile.birthday = birthday;
	    profile.gender = gender;
		profile.interestedIn = interestedIn;
	    profile.relationshipStatus = relationshipStatus;

		Language lang = Language.find("name = ?", language).first();
		if (lang == null){
			lang = new Language(language);
			lang.save();
		}
		UserLanguage userlang = new UserLanguage(user, lang);
		userlang.save();
	  profile.languages.add(userlang);

	  profile.political = political;
		profile.save();
		renderTemplate("Application/edit_basic.html", profile);
	}
	
	public static void updateContactInfo(String phone, String address){
		User user = user();
		Profile profile = Profile.find("owner = ?", user).first();
		profile.phone = phone;
		profile.address = address;
		profile.save();
		renderTemplate("Application/edit_basic.html", profile);	
	}
  
	public static void updateWorkEdu(String education, String work){
		User user = user();
		Profile profile = Profile.find("owner = ?", user).first();
		//TODO: write this!
		renderTemplate("Application/edit_basic.html", profile);	
	}

  public static void updateBasic(String religion, String bio, Date birthday, String gender,
      String relationshipStatus, String language, String political, String phone,
      String address, List<Enrollment> education, List<Employment> work, Location hometown,
      String quotes) {
	User user = user();
	Profile profile = user.profile;
    profile.religion = religion;
    profile.birthday = birthday;
    profile.gender = gender;
    profile.relationshipStatus = relationshipStatus;

	Language lang = Language.find("name = ?", language).first();
	if (lang == null){
		lang = new Language(language);
		lang.save();
	}
	UserLanguage userlang = new UserLanguage(user, lang);
	userlang.save();
    profile.languages.add(userlang);

    profile.political = political;
    profile.phone = phone;
    profile.address = address;
    profile.education = education;
    profile.work = work;
  //  u.profile.hometown = hometown;
    profile.quotes = quotes;
    profile.save();
    user.save();
    renderTemplate("Application/edit_basic.html", profile);
  }
}