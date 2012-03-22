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

	  public static void addLike (Long commentId, Long userId){
	    Like newOne = new Like ((Likeable)Comment.findById(commentId),(User)User.findById(userId)).save();
	    Comment c = Comment.findById(commentId);
	    c.addLike(newOne);
	    comments(userId);
	  }
	  
	  public static void unLike (Long commentId, Long userId){
	    Comment c = Comment.findById(commentId);
	    User u = User.findById(userId);
	    Like toRemove = Like.find("author = ? AND comment = ?", u, c).first();
      c.removeLike(toRemove);
	    comments(userId);
	  }
	  
	  public static void postThreadComment(Long threadId, String thread_content){
	    FThread thread = ((FThread) FThread.findById(threadId));
      thread.addComment(Application.user(), thread_content);
      
      renderTemplate("FThreads/listSingle.html", thread);
	  }
	  
}
