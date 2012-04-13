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

  public static void about(Long id) {
    User user = id == null ? user() : (User) User.findById(id);
    render(user);
  }

  public static void news(Long id) {
    User user = id == null ? user() : (User) User.findById(id);
    render(user);
  }
  
  public static void friendRequests() {
    User user = user();
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
      r1.save();
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
    System.out.println(r1.requested);
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
    validation.isTrue(
        currentUser.password.equals(Crypto.passwordHash(old_password)))
        .message("Password does not match");

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
    user.profile.save();
    renderTemplate("Application/edit_basic.html", user);
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

  public static void notFound() {
    response.status = Http.StatusCode.NOT_FOUND;
    renderText("");
  }

  public static void addLike (Long likeableId, Long userId){
    Likes newOne = new Likes ((Likeable)Likeable.findById(likeableId),(User)User.findById(userId)).save();
    Likeable l = Likeable.findById(likeableId);
    l.addLike(newOne);
    news(userId);
  }
  
  public static void unLike (Long likeableId, Long userId){
    Likeable c = Likeable.findById(likeableId);
    User u = User.findById(userId);
    Likes toRemove = Likes.find("author = ? AND parentObj = ?", u, c).first();
    c.removeLike(toRemove);
    news(userId);
  }
  
  public static void addLikeAjax (Long likeableId){
    User u = user();
    Likeable l = Likeable.findById(likeableId);
    l.addLike(u);
    Map<String,String> m = new HashMap<String,String>();
    m.put("numLikes", Integer.toString(l.likes.size()));
    m.put("likeableID",likeableId.toString());
    renderJSON(m);
  }
  
  public static void removeLikeAjax (Long likeableId){
    User u = user();
    Likeable l = Likeable.findById(likeableId);
    l.removeLike(u);
    Map<String,String> m = new HashMap<String,String>();
    m.put("numLikes", Integer.toString(l.likes.size()));
    m.put("likeableID",likeableId.toString());
    renderJSON(m);
  }

}
