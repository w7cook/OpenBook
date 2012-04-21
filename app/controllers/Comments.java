package controllers;

import java.util.*;

import play.*;
import play.db.jpa.GenericModel.JPAQuery;
import play.mvc.*;
import play.utils.HTML;
import controllers.Secure;
import models.*;

public class Comments extends OBController {

  //comments(Long id): will render the user being viewed unless it is a null user then it will render the current user
  public static void comments(Long userId) {
    User user = userId == null ? Application.user() : (User) User.findById(userId);
    List<Comment> comments;
    if (userId == null)
      comments = Comment.findAll();
    else
      comments = user.comments();
    render(user, comments);
  }

  public static void comment(Long commentId) {
    User user = user();
    Comment comment = Comment.findById(commentId);
    if(comment != null)
      render(comment, user);
    else
      notFound("That comment does not exist.");
  }

  public static void deleteComment(Long id, Long userId) {
    Comment c = Comment.findById(id);
    c.delete();
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
