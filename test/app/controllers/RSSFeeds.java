package controllers;

import org.hibernate.exception.ConstraintViolationException;

import java.util.*;

import org.w3c.dom.Document;

import play.*;
import play.db.jpa.GenericModel.JPAQuery;
import play.libs.WS;
import play.mvc.*;
import play.utils.HTML;
import controllers.Secure;
import models.*;

public class RSSFeeds extends OBController {
	
	public static void addFeed(String url) {
		if(url != null && !url.trim().equals("")){
			User u = user();
			RSSFeed feed = new RSSFeed(u, url.trim());
      if (feed.is_valid) {
        try {
          feed.save();
          renderJSON("1");
        } catch (ConstraintViolationException e) {
        }
      }
		}
    renderJSON("0");
	}
	public static void RSSfeeds(Long userId) {
		User current = User.findById(userId);
		if (current != null){
			List<RSSFeed> feeds = new ArrayList<RSSFeed>();

			for(RSSFeed f : current.feeds) {
        if (!f.is_valid) continue;
				feeds.add(f);
			}
			render(feeds);
		}
	}
}

