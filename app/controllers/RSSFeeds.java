package controllers;

import java.util.*;

import play.*;
import play.db.jpa.GenericModel.JPAQuery;
import play.mvc.*;
import play.utils.HTML;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class RSSFeeds extends OBController {
  public static void addFeed(String url) {
    User u = user();
    RSSFeed feed = new RSSFeed(u, url);
    feed.save();

    if (feed.is_valid) {
      System.out.println("Feed is Valid "+feed.url);
      for(RSSFeed.RSSFeedNode n : feed.getItems()) {
        System.out.println(n.ccontent("title"));
      }
      System.out.println("\n\n");
    }

  }
}

