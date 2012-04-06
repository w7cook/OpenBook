package controllers;

import java.util.*;

import play.*;
import play.db.jpa.GenericModel.JPAQuery;
import play.mvc.*;
import play.utils.HTML;
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

  public static void addLike (Long commentId, Long userId){
    Likes newOne = new Likes ((Likeable)Comment.findById(commentId),(User)User.findById(userId)).save();
    Comment c = Comment.findById(commentId);
    c.addLike(newOne);
    comments(userId);
  }
  
  public static void unLike (Long commentId, Long userId){
    Comment c = Comment.findById(commentId);
    User u = User.findById(userId);
    Likes toRemove = Likes.find("author = ? AND parentObj = ?", u, c).first();
          c.removeLike(toRemove);
    comments(userId);
  }
	  
  public static void postComment(Long statusId, Long userId, String commentContent) {
    ((Status) Status.findById(statusId)).addComment(Application.user(), commentContent);
    comments(userId);
  }

  public static void makeNewComment(String commentContent, String statusId, String userId) {
    final Commentable cc = Commentable.findById(Long.parseLong(statusId));
    final User u = User.findById(Long.parseLong(userId));
    final Comment c = new Comment(cc, u, HTML.htmlEscape(commentContent)).save();
    Map<String, Object> m = new HashMap<String, Object>();
    m.put("comment", c);
    m.put("user", user());
    m.put("currentUser", user());
    renderTemplate(m);
  }
}
