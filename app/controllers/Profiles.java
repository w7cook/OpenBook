package controllers;

import java.text.*;
import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;
import models.Profile.Relationship;

import models.Enrollment;
import models.Language;
import models.Location;
import models.Profile;
import models.User;
import models.UserLanguage;

import org.apache.ivy.util.cli.ParseException;

import play.data.validation.Error;

public class Profiles extends OBController {

  public static void updateBio(String bio){
    User user  = user();
    Profile profile = Profile.find("owner = ?", user).first();

    if(!bio.equals("Write About Yourself"))
      profile.bio = bio;
    else
      profile.bio = "";

    profile.save();
    renderTemplate("Application/edit_basic.html", profile);
  }

  public static void updateInformation(String birthday, String relationshipStatus, String gender, String interestedIn,
                                       String anniversary, String language, String religion, String political){
    User user = user();
    Profile profile = Profile.find("owner = ?", user).first();
    profile.religion = religion;
    DateFormat birthday_formatting = new SimpleDateFormat("MM/dd/yyyy");
    try{
      profile.birthday = (Date) birthday_formatting.parse(birthday);
    } catch (java.text.ParseException e) {
      e.printStackTrace();
    }

    profile.gender = gender;
    profile.interestedIn = interestedIn;
    DateFormat anniversary_formatting = new SimpleDateFormat("MM/dd/yyyy");
    try {
      profile.anniversary = (Date) anniversary_formatting.parse(anniversary);
    } catch (java.text.ParseException e) {
      e.printStackTrace();
    }
    //        profile.relationshipStatus = relationshipStatus;

    Language lang = Language.find("name = ?", language).first();
    UserLanguage userlang = new UserLanguage(profile, lang);
    if (lang != null){
      userlang.save();
      profile.languages.add(userlang);
    }

    profile.political = political;
    profile.save();
    renderTemplate("Application/edit_basic.html", profile);
  }

  public static void updateContactInfo(String phone, String address){
    User user = user();
    Profile profile = Profile.find("owner = ?", user).first();

    if(!phone.equals("Add A Phone Number")){
      profile.phone = phone;
    }
    else
      profile.phone = "";
    validation.phone(profile.phone);
    if(validation.hasErrors()) {
      for(Error error: validation.errors()){
        System.out.println("\n\n\n\n" + error.message() + "\n\n\n\n");
      }
      profile.phone = "Add A Phone Number";
    }

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

    if(!hometown.equals("Add Hometown")){
      Location loc = Location.find("location = ?", hometown).first();
      if(loc == null){
        loc = new Location(hometown);
        loc.save();
      }
      profile.hometown = loc;
    }
    else
      profile.hometown = null;

    profile.save();
    renderTemplate("Application/edit_basic.html", profile);
  }

  public static void updateQuote(String quotation){
    User user = user();
    Profile profile = Profile.find("owner = ?", user).first();

    if(!quotation.equals("Add a Favorite Quotation"))
      profile.quotes = quotation;
    else
      profile.quotes = "";

    profile.save();
    renderTemplate("Application/edit_basic.html", profile);
  }
}