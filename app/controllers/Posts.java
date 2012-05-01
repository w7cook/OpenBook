package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

public class Posts extends OBController {
  //comments(Long id): will render the user being viewed unless it is a null user then it will render the current user
  public static void posts(Long userId) {
    User user = userId == null ? user() : (User) User.findById(userId);
    List<Post> posts = userId == null ? user.news() : user.posts;
    render(user, posts);
  }

  public static void post(Long postId) {
    User user = user();
    Post post = Post.findById(postId);
    if(post != null)
      render(post, user);
    else
      notFound("That post does not exist.");
  }

  public static void deletePost(Long postId) {
    User user = user();
    Post p = Post.findById(postId);
    if (p == null)
      notFound();
    if (!p.owner.equals(user))
      forbidden();
    p.delete();
    ok();
  }

  public static void makeNewPost(String postContent) {
    if (postContent == null)
      error("postContent can't be null");
    User user = user();
    final Post post = new Post(user, user, postContent).save();
    render(user, post);
  }

  public static void makeNewPagePost(String postContent, String pid) {
    Page page = Page.findById(Long.parseLong(pid));
    final Post p = new Post(page, user(), postContent).save();
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("item", p);
    m.put("user", user());
    m.put("currentUser", user());
    renderTemplate(m);
  }

  public static void makeNewGroupPost(String postContent, String gid) {
    Group group = Group.findById(Long.parseLong(gid));
    final Post p = new Post(group, user(), postContent).save();
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("item", p);
    m.put("user", user());
    m.put("currentUser", user());
    renderTemplate(m);
  }

  public static void poke(Long userId) {
    String poked = new String(user() + " has poked " + (User)User.findById(userId) + "!");
    new Post(user(), user(), poked).save();
    posts(userId);
  }
}
