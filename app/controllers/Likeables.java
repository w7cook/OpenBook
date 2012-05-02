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
    if(thing.addLike(user()))
      ok();
    notModified();
  }

  public static void unLike (Long likeableId) {
    Likeable thing = Likeable.findById(likeableId);
    if(thing == null)
      notFound();
    if(thing.removeLike(user()))
      ok();
    notModified();
  }
}