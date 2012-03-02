package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public abstract class OBController extends Controller {

  @Before
  static void setConnectedUser() {
    if (Security.isConnected()) {
      renderArgs.put("currentUser", user());
    }
  }

  @Before
  static void addDefaults() {
  }

  public static User user() {
    assert Secure.Security.connected() != null;
    return User.getUser(Secure.Security.connected());
  }
}