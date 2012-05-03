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
    redirect("/");
  }
  public static void RSSfeeds(Long userId) {
    User current = User.findById(userId);
    if (current == null)
      notFound();
    List<RSSFeed> feeds = new ArrayList<RSSFeed>();
    for(RSSFeed f : current.feeds) {
      feeds.add(f);
    }

    render(feeds);
  }
}

