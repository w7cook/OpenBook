package controllers;

import java.util.*;

import controllers.Secure;

import play.*;
import play.data.validation.Required;
import play.mvc.*;
import controllers.Secure;
import models.*;

public class Checkin extends OBController {

  public static void checkin() {
    render();
  }

  public static void at(String location, String name, String address) {
    if (location != null && name != null && address != null
        && !location.equals("") && !name.equals("") && !address.equals("")) {
      Posts.newPost(user().id, "Checked in at: "+name+"\n"+address);
    }
    else {
      redirect("/checkin");
    }
  }
}
