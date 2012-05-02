package controllers;

import java.util.*;

import play.*;
import play.db.jpa.GenericModel.JPAQuery;
import play.mvc.*;
import play.utils.HTML;
import controllers.Secure;
import models.*;

public class RSSFeeds extends OBController {
  public static void addFeed(String url) {
    User u = user();
    RSSFeed feed = new RSSFeed(u, url);
    // Add Unique Constraint Check in model
    feed.save();
    ok();
  }
}

