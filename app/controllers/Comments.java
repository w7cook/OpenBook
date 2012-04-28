package controllers;

import java.util.*;

import play.*;
import play.db.jpa.*;
import play.mvc.*;

import models.*;

public class Comments extends OBController {
  public static void comments(Long statusId) {
    User user = user();
    List<Comment> comments;
    if (statusId == null)
      comments = Comment.findAll();
    else {
      Commentable c = Commentable.findById(statusId);
      if (c == null)
        notFound();
      comments = Comment.find("byParentObj", c).fetch();
    }
    render(user, comments);
  }

  //comments(Long id): will render the user being viewed unless it is a null user then it will render the current user
  public static void userComments(Long userId) {
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
    if(comment == null)
      notFound("That comment does not exist.");
    render(user, comment);
  }

  public static void deleteComment(Long commentId) {
    Comment c = Comment.findById(commentId);
    if (c == null)
      notFound("That comment does not exist.");
    c.delete();
    ok();
  }

  public static void addComment(Long statusId, String commentContent) {
    if (commentContent == null)
      error("commentContent can't be null");
    final Commentable cc = Commentable.findById(statusId);
    if(cc == null)
      notFound(statusId + " is not the id of a Commentable");
    final User user = user();
    final Comment comment = new Comment(cc, user, commentContent).save();
    render(user, comment);
  }
}