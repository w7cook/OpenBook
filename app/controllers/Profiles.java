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
		Profile profile = user.profile;
		
		if(!bio.equals("Write About Yourself"))
			profile.bio = bio;
		else
			profile.bio = "";
		
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
		UserLanguage userlang = new UserLanguage(profile, lang);
		userlang.save();
	    profile.languages.add(userlang);

	    profile.political = political;
		profile.save();
		renderTemplate("Application/edit_basic.html", profile);
	}
	
	public static void updateContactInfo(String phone, String address){
		User user = user();
		Profile profile = user.profile;
		
		if(!phone.equals("Add A Phone Number"))
			profile.phone = phone;
		else
			profile.phone = "";
		
		if(!address.equals("Add Current Address"))
			profile.address = address;
		else
			profile.address = "";
		
		profile.save();
		renderTemplate("Application/edit_basic.html", profile);	
	}
  
	public static void updateWorkEdu(String edu, String work){
		User user = user();
		Profile profile = user.profile;
		
		if(!edu.equals("Add A School")){
			String[] schools = edu.split(",");
			for (String school : schools){
				if(!school.equals("Add A School")){
					Enrollment enrollment = Enrollment.find("byName", school).first();
					if (enrollment == null){
						enrollment = new Enrollment(school);
						enrollment.student = profile;
						enrollment.save();
					}
					if(!profile.education.contains(enrollment))
						profile.education.add(enrollment);
				}		
			}
		}
		else
			profile.education = new ArrayList<Enrollment>();
	
		/*
		Employment employment = new Employment(work);
		employment.employee = profile;
		employment.save();
		profile.work.add(employment);
		*/
		profile.save();
		renderTemplate("Application/edit_basic.html", profile);	
	}
	
	public static void updateLiving(String hometown){
		User user = user();
		Profile profile = user.profile;
		
		Location loc = Location.find("byLocation", hometown).first();
		if(loc == null){
			loc = new Location(hometown);
			loc.save();
		}
		profile.hometown = loc;
		
		profile.save();
		renderTemplate("Application/edit_basic.html", profile);
	}
	
	public static void updateQuote(String quotation){
		User user = user();
		Profile profile = user.profile;
		
		if(!quotation.equals("Add a Favorite Quotation"))
			profile.quotes = quotation;
		else
			profile.quotes = "";
			
		profile.save();
		renderTemplate("Application/edit_basic.html", profile);	
	}


/*
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
  }*/
}