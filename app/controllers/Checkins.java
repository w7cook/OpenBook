package controllers;

import java.util.*;

import controllers.Secure;

import play.*;
import play.data.validation.Required;
import play.mvc.*;
import play.utils.HTML;
import controllers.Secure;
import models.*;

public class Checkins extends OBController {

  public static void checkin() {
    render();
  }

  public static void at(String location, String name, String address) {
    if (location != null && name != null && address != null
        && !location.equals("") && !name.equals("") && !address.equals("")) {
      new Checkin(user(), user(), location, name, address).save();
      Application.news(null);
    }
    else {
      redirect("/checkin");
    }
  }
}
