package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Profiles extends OBController {
  public static void updateBasic(Long userId, String religion) {
    User u = User.findById(userId);
    u.profile.religion = religion;
    u.profile.save();
  }
}