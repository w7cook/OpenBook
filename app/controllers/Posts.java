package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Posts extends OBController {

	//comments(Long id): will render the user being viewed unless it is a null user then it will render the current user
	public static void posts(Long id) {
		User user = id == null ? user() : (User) User.findById(id);
		render(user);
	}

	public static void deletePost(Long id, Long userId) {
		Post p = Post.findById(id);
		p.delete();
		posts(userId);
	}

  public static void newPost(Long userId, String post_content) {
    new Post((User)User.findById(userId), new Date().toString(), post_content).save();
    posts(userId);
  }

  public static void makeNewPost(String postContent) {
    final Post p = new Post(user(), new Date().toString(), postContent).save();
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("item", p);
    m.put("user", user());
    m.put("currentUser", user());
    renderTemplate(m);
  }
}

