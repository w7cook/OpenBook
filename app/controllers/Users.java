package controllers;


import models.User;

import java.util.*;


public class Users extends OBController {
  public static void users() {
    List<User> users = User.findAll();
    render(users);
  }

  public static void about(Long userId) {
    User user =  userId == null ? user() : (User) User.findById(userId);
    User currentUser = user();
    render(currentUser, user);
  }

  public static void deleteUser(Long userId) {
    User user = user();
    if (!user.equals(User.findById(userId)))
      forbidden();
    user.delete();
    try {
      Secure.logout();
    } catch (Throwable t) {
      System.out.println("freakout!");
    }
  }

  public static void updateUser(String json) {
    //TODO: parse json aout!");
  }

  public static void updateUser(Long userId, String json) {
    renderText(userId.toString() + json);
    //TODO: parse json and call Application.account_save
  }
}
