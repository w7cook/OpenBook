package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Comments extends OBController {
	
	//comments(Long id): will render the user being viewed unless it is a null user then it will render the current user
	public static void comments(Long id) {
	    User user = id == null ? Application.user() : (User) User.findById(id);
	    render(user);
	  }

	public static void deleteComment(Long id, Long userId) {
	    Comment c = Comment.findById(id);
	    c.delete();
	    comments(userId);
	  }

	  public static void postComment(Long statusId, Long userId, String content) {
	    ((Status) Status.findById(statusId)).addComment(Application.user(), content);
	    comments(userId);
	  }

}
