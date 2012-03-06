package controllers;

import java.util.*;

import org.w3c.dom.UserDataHandler;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Comments extends OBController {
	
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
	public static void comments(Long id) {
	    User user = id == null ? user() : (User) User.findById(id);
	    render(user);
	  }
	
	public static User user() {
	    assert Secure.Security.connected() != null;
	    boolean isUsername = User.find("byEmail", Secure.Security.connected()).first() == null;	    
	    if(isUsername)
	      return User.find("byUsername", Secure.Security.connected()).first();
	    else
	      return User.find("byEmail", Secure.Security.connected()).first();    
	 }

	public static void deleteComment(Long id, Long userId) {
	    Comment c = Comment.findById(id);
	    c.delete();
	    comments(userId);
	  }

	  public static void postComment(Long commentableId, Long userId, String content) {
	    ((Commentable) Commentable.findById(commentableId)).addComment(user().first_name, content);
	    comments(userId);
	  }

}