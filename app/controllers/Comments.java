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
