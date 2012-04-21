package controllers;

import java.util.*;

import play.*;
import play.db.jpa.*;
import play.mvc.*;
import models.*;


public class Likeables extends OBController {
  public static void like (Long likeableId) {
    Likeable thing = Likeable.findById(likeableId);
    if(thing == null)
      notFound();
    thing.addLike(user());
    ok();
  }

  public static void unLike (Long likeableId) {
    Likeable thing = Likeable.findById(likeableId);
    if(thing == null)
      notFound();
    thing.removeLike(user());
    ok();
  }
}