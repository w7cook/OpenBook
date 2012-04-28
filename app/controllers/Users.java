package controllers;

import models.*;

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
      unauthorized();
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

  public static List<String> fullNames() {
    List<User> users = User.findAll();
    List<String> names = new ArrayList<String>();
    for(User u : users){
      names.add(u.name);
    }
    return names;
  }

  public static void friendRequests(Long userId) {
    User user = user();
    if (userId != null && userId != user.id)
      unauthorized();
    render(user);
  }

  public static void autocompleteNames(final String term) {
    final List<String> response = new ArrayList<String>();
    for (String name : fullNames()) {
      if (name.toLowerCase().startsWith(term.toLowerCase())) {
        response.add(name);
      }
      if (response.size() == 10) {
        break;
      }
    }
    renderJSON(response);
  }

  public static void profile(Long userId) {
    User user = userId == null ? user() : (User)User.findById(userId);
    if(user == null)
      notFound();
    Profile profile = user.profile;
    if(profile == null)
      notFound();
    render(user, profile);
  }

  /** Request to be friends with a given user, changing appropriate Relationship flags where necessary.
   *
   * @param userId the user to request friendship with
   */

  public static void requestFriends(Long userId) {
    User user = user();
    User other = User.findById(userId);
    if(other == null)
      notFound();
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
        Application.news(userId);
        return;
      } else {
        r1.requested = true;
      }
    }
    // If the user has already made a request for friendship, do nothing
    else if (r1 != null) {
      if (r1.requested) {
        Application.news(userId);
        return;
      } else {
        r1.requested = true;
        r1.save();
        Application.news(userId);
        return;
      }
    }
    System.out.println(r1.requested);
    r1.save();
    Application.news(userId);
  }

  /** Attempt to end a relationship with a user, changing appropriate Relationship flags where necessary
   *
   * @param userId the user to remove
   */
  public static void removeFriends(Long userId) {
    User user = user();
    User other = User.findById(userId);
    if (other == null)
      notFound();
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
    Application.news(userId);
  }
}
