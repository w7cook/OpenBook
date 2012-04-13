package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
//import controllers.Secure;
import models.*;

@With(OBSecure.class)
public abstract class OBController extends Controller {

  @Before
  static void setConnectedUser() {
    if (Security.isConnected() || request.user != null) {
      renderArgs.put("currentUser", user());
    }
  }

  @Before
  static void addDefaults() {
  }

  public static User user() {
    if (request.user != null) {
      return User.connect(request.user, request.password);
    }
    assert Secure.Security.connected() != null;
    return User.getUser(Secure.Security.connected());
  }
}
