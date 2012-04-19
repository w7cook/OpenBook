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
    user.profile.religion = religion;
    user.profile.birthday = birthday;
    user.profile.gender = gender;
    user.profile.relationshipStatus = "Single";
    user.profile.languages = languages;
    user.profile.political = political;
    user.profile.phone = phone;
    user.profile.address = address;
    user.profile.education = education;
    user.profile.work = work;
  //  u.profile.hometown = hometown;
    user.profile.quotes = quotes;
    user.profile.save();
    user.save();
    System.out.println("\n\n\n\n*****" + user.profile.religion + "\n\n ******");
    renderTemplate("Application/edit_basic.html", user);
  }
}