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
    if (!p.author.equals(user))
      forbidden();
    p.delete();
    posts(user.id);
  }

  public static void makeNewPost(String postContent) {
    final Post p = new Post(user(), user(),
                            HTML.htmlEscape(postContent)).save();
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("item", p);
    m.put("user", user());
    m.put("currentUser", user());
    renderTemplate(m);
  }

  public static void makeNewPagePost(String postContent, String pid) {
    Page page = Page.findById(Long.parseLong(pid));
    final Post p = new Post(page, user(),
        HTML.htmlEscape(postContent)).save();
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("item", p);
    m.put("user", user());
    m.put("currentUser", user());
    renderTemplate(m);
  }
  
  public static void makeNewGroupPost(String postContent, String gid) {
	    Group group = Group.findById(Long.parseLong(gid));
	    final Post p = new Post(group, user(),
	        HTML.htmlEscape(postContent)).save();
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
