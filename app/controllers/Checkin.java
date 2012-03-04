package controllers;

import java.util.*;

import controllers.Secure;

import play.*;
import play.data.validation.Required;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Checkin extends Application {

  public static void checkin() {
    render();
  }

  public static void at(String location, String name, String address) {
    if (location != null && name != null && address != null
        && !location.equals("") && !name.equals("") && !address.equals("")) {
      render(name, address);
    }
    else {
      redirect("/checkin");
    }
  }

}
