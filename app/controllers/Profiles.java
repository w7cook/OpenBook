package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Profiles extends OBController {
  
  public static void updateBasic(User user, String religion, Date birthday, String gender,
      String relationshipStatus, List<UserLanguage> languages, String political, String phone,
      String address, List<Enrollment> education, List<Employment> work, Location hometown,
      String quotes) {
    User u = user;
    u.profile.religion = religion;
    u.profile.birthday = birthday;
    u.profile.gender = gender;
    u.profile.relationshipStatus = "Single";
    u.profile.languages = languages;
    u.profile.political = political;
    u.profile.phone = phone;
    u.profile.address = address;
    u.profile.education = education;
    u.profile.work = work;
  //  u.profile.hometown = hometown;
    u.profile.quotes = quotes;
    u.profile.save();
    u.save();
    System.out.println("Relgion = " + religion);
    renderTemplate("Application/edit_basic.html", u);
  }
}