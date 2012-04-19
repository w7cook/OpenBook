package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import play.utils.HTML;
import controllers.Secure;
import models.*;

public class Posts extends OBController {
  //comments(Long id): will render the user being viewed unless it is a null user then it will render the current user
  public static void posts(Long userId) {
    User user = userId == null ? user() : (User) User.findById(userId);
    List<Post> posts = userId == null ? user.news() : user.posts();
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
    if (!p.author.equals(user))
      forbidden();
    p.delete();
    posts(user.id);
  }

  public static void newPost(Long userId, String post_content) {
    new Post((User)User.findById(userId), new Date().toString(), post_content).save();
    posts(userId);
  }

  public static void makeNewPost(String postContent) {
    User user = user();
    final Post post = new Post(user(), new Date().toString(),
                            HTML.htmlEscape(postContent)).save();
    render(post, user);
  }

  public static void makeNewPagePost(String postContent, String pid) {
    final Post p = new Post(user(), HTML.htmlEscape(pid),
        HTML.htmlEscape(postContent),Post.type.PAGE).save();
    Page page = Page.findById(Long.parseLong(pid));
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("item", p);
    m.put("user", user());
    m.put("currentUser", user());
    renderTemplate(m);
  }

  public static void poke (Long userId) {
    String poked = new String(user() + " has poked " + (User)User.findById(userId) + "!");
    new Post(user(), new Date().toString(), poked).save();
    posts(userId);
  }
}
