package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Application extends Controller {

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
    return User.find("byEmail", Secure.Security.connected()).first();
  }

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

  public static void account_save(User update, String old_password) {
    User currentUser = user();

    validation.required(update.first_name).message("First name is required");
    validation.required(update.username).message("Username is required");
    validation.required(update.email).message("Email is required");
    validation.isTrue(currentUser.password.equals(old_password)).message(
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
        name += user.first_name;
      }
      if (given(update.last_name)) {
        user.last_name = update.last_name;
        if (given(name))
          name += " ";
        name += user.first_name;
      }
      user.name = name;
      user.username = update.username;
      user.email = update.email;
      if (given(update.password))
        user.password = update.password;
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

  public static void deleteComment(Long id, Long userId) {
    Comment c = Comment.findById(id);
    c.delete();
    news(userId);
  }

  public static void postComment(Long postId, String author, String content) {
    Post post = Post.findById(postId);
    post.addComment(author, content);
  }
}
