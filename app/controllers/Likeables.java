package controllers;

import java.util.*;

import play.*;
import play.db.jpa.*;
import play.mvc.*;
import models.*;


public class Likeables extends OBController {
  public static void likes (Long likeableId) {
    User user = user();
    Likeable thing = Likeable.findById(likeableId);
    if(thing == null)
      notFound();
    render(user, thing);
  }

  public static void like (Long likeableId) {
    Likeable thing = Likeable.findById(likeableId);
    if(thing == null)
      notFound();
    thing.addLike(user());
    likes(thing.id);
  }

  public static void unLike (Long likeableId) {
    Likeable thing = Likeable.findById(likeableId);
    if(thing == null)
      notFound();
    thing.removeLike(user());
    likes(thing.id);
  }
}