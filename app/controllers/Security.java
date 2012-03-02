package controllers;

import models.*;

public class Security extends Secure.Security {
  public static String connected() {
    return Secure.Security.connected();
  }

  static boolean authenticate(String username, String password) {
    return User.connect(username, password) != null;
  }
  static boolean authentify(String username, String password) {
    return User.connect(username, password) != null;
  }
}
