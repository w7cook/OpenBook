package controllers;

import java.util.*;

import org.elasticsearch.index.query.QueryBuilders;

import play.*;
import play.modules.elasticsearch.ElasticSearch;
import play.modules.elasticsearch.search.SearchResults;
import play.mvc.*;
import controllers.Secure;
import models.*;
import play.libs.Crypto;


public class Application extends OBController {
  public static void news(Long userId) {
    User user = userId == null ? user() : (User) User.findById(userId);
    render(user);
  }

  public static void account() {
    User user = user();
    render(user);
  }

  private static boolean given(String val) {
    return val != null && val.length() > 0;
  }


  /**
   * Create a timeline for the current user
   * @param id user ID
   */
  public static void createTimeline(Long id){
    User user = User.findById(id);
    user.createTimeline();
    renderTemplate("Timeline/Timeline.html",user);
  }

  public static void account_save(User update, String old_password) {
    User currentUser = user();

    validation.required(update.first_name).message("First name is required");
    validation.required(update.username).message("Username is required");
    validation.required(update.email).message("Email is required");
    validation.isTrue(currentUser.password.equals(Crypto.passwordHash(old_password))).message("Password does not match");

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

  public static void search(String query) {
    // not implemented yet
  }

  public static void deleteComment(Long id, Long userId) {
    Comment c = Comment.findById(id);
    c.delete();
    news(userId);
  }

  public static void postComment(Long commentableId, String author, String content) {
    Commentable parent = Commentable.findById(commentableId);
    User au = User.find("email = ?", author).first();
    parent.addComment(au, content);
  }
}