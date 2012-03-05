package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Posts extends Controller {
	
	@Before
	static void setConnectedUser() {
	  if (Security.isConnected()) {
	    renderArgs.put("currentUser", user());
	  }
	}
	
	@Before
	static void addDefaults() {
	 }
	
	//comments(Long id): will render the user being viewed unless it is a null user then it will render the current user
	public static void posts(Long id) {
	    User user = id == null ? user() : (User) User.findById(id);
	    render(user);
	  }
	
	public static User user() {
	    assert Secure.Security.connected() != null;
	    return User.find("byEmail", Secure.Security.connected()).first();
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

}