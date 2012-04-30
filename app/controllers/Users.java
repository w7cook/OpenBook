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

  public static void friends(Long userId) {
    User user = User.findById(userId);
    User currentUser = user();
    if (user == null)
      notFound();
    if (!(user.isFriendsWith(currentUser) || user.equals(currentUser)))
      unauthorized();
    Set<User> friends = new HashSet(user.friends);
    friends.remove(user);
    render(currentUser, friends);
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

  /** Request to be friends with a given user
   *
   * @param userId the user to request friendship with
   */

  public static void requestFriends(Long userId) {
    User user = user();
    User other = User.findById(userId);
    if(other == null)
      notFound();

    // I know it's redundant, but it's helpful for testing purposes
    if(user.equals(other)) {
      user.friends.add(other);
      user.save();
      renderText("friends");
    }

    // If the other person has already requested friendship with you
    if (user.friendRequests.contains(other)) {
      user.friendRequests.remove(other);
      user.friends.add(other);
      other.friends.add(user);
      user.save();
      other.save();
      renderText("friends");
    }
    other.friendRequests.add(user);
    other.save();
    renderText("requested");
  }

  /** Attempt to end a relationship with a user
   *
   * @param userId the user to remove
   */
  public static void removeFriends(Long userId) {
    User user = user();
    User other = User.findById(userId);
    if (other == null)
      notFound();
    other.friends.remove(user);
    user.friends.remove(other);
    user.friendRequests.remove(other);
    other.friendRequests.remove(user);
    user.save();
    other.save();
    ok();
  }
}