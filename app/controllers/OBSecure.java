package controllers;

import play.mvc.*;
import models.*;

public class OBSecure extends Secure {
  @Before(unless={"login", "authenticate", "logout"})
  static void checkAccess() throws Throwable {
    if (request.user != null)
      if (User.connect(request.user, request.password) != null)
        session.put("username", request.user);
    Secure.checkAccess();
  }
}