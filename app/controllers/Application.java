package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;
import play.libs.Crypto;

public class Application extends OBController {
  public static void about(Long id) {
    User user = id == null ? user() : (User) User.findById(id);
    render(user);
  }

  public static void news(Long id) {
    User user = id == null ? user() : (User) User.findById(id);
    render(user);
  }

  public static void account() {
    User user = user();
    render(user);
  }

  private static boolean given(String val) {
    return val != null && val.length() > 0;
  }

  /** Request to be friends with a given user, changing appropriate Relationship flags where necessary.
   *
   * @param id the user to request friendship with
   */
  public static void requestFriends(Long id) {
    User user = user();
    User other = User.findById(id);
    Relationship r1 = Relationship.find("SELECT r FROM Relationship r where r.from = ? AND r.to = ?", user, other).first();
    Relationship r2 = Relationship.find("SELECT r FROM Relationship r where r.to = ? AND r.from = ?", user, other).first();

    if (r1 == null) {
      r1 = new Relationship(user, other, true);
    }

    if (r2 != null) {

      // If the other user has already requested, this request should make them friends
      if (r2.requested) {
        r1.requested = false;
        r2.requested = false;
        r1.accepted = true;
        r2.accepted = true;
        r1.save();
        r2.save();
      } else if (r2.accepted){
        news(id);
        return;
      } else {
        r1.requested = true;
      }
    }
    // If the user has already made a request for friendship, do nothing
    else if (r1 != null) {
      if (r1.requested) {
        news(id);
        return;
      } else {
        r1.requested = true;
        r1.save();
        news(id);
        return;
      }
    }
    r1.save();
    news(id);
  }

  /** Attempt to end a relationship with a user, changing appropriate Relationship flags where necessary
   *
   * @param id the user to remove
   */
  public static void removeFriends(Long id) {
    User user = user();
    User other = User.findById(id);
    Relationship r1 = Relationship.find("SELECT r FROM Relationship r where r.from = ? AND r.to = ?", user, other).first();
    Relationship r2 = Relationship.find("SELECT r FROM Relationship r where r.to = ? AND r.from = ?", user, other).first();

    if (r1 != null) {
      r1.requested = false;
      r1.accepted = false;
      r1.save();
    }
    if (r2 != null) {
      r2.accepted = false;
      r2.requested = false;
      r2.save();
    }
    news(id);
  }

  public static void account_save(User update, String old_password) {
    User currentUser = user();

    validation.required(update.first_name).message("First name is required");
    validation.required(update.username).message("Username is required");
    validation.required(update.email).message("Email is required");
    validation.isTrue(currentUser.password.equals(Crypto.passwordHash(old_password))).message(
                                                                                              "Password does not match");

    if (validation.hasErrors()) {
      User user = update;
      renderTemplate("Application/account.html", user);
    } else {
      User user = currentUser;
      String name = "";
      if (given(update.first_name)) {
        user.first_name = update.first_name;
        name += user.first_name;
      }
      if (given(update.middle_name)) {
        user.middle_name = update.middle_name;
        if (given(name))
          name += " ";
        name += user.middle_name;
      }
      if (given(update.last_name)) {
        user.last_name = update.last_name;
        if (given(name))
          name += " ";
        name += user.last_name;
      }
      user.name = name;
      user.username = update.username;
      user.email = update.email;
      if (given(update.password))
        user.password = Crypto.passwordHash(update.password);
      user.save();
      account();
    }
  }
  
  public static void edit_basic() {
    long userID = 1;
    User user = User.findById(userID);
    render(user);
  }

  public static void updateBasic() {
    long userID = 1;
    User user = User.findById(userID);
    render(user);
  }

  public static void search(String query) {
    // not implemented yet
  }

  public static void notFound() {
    response.status = Http.StatusCode.NOT_FOUND;
    renderText("");
  }

}
