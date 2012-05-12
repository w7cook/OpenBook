package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Subscriptions extends OBController {

	public static void addSubscription(Long userId) {
		User user = user();
		User other = User.findById(userId);
		if(other == null)
		      notFound();
		Subscription s = Subscription.find("SELECT s FROM Subscription s WHERE s.subscriber = ?1 AND s.subscribed = ?2", user, other).first();
		if(s == null) {
			s = new Subscription(other, user).save();
			user.subscribedTo.add(s);
		    other.subscribers.add(s);
		    user.save();
		    other.save();
		}
		subscriptions(other.id);
	}
	
	public static void removeSubscription(Long userId) {
		User user = user();
		User other = User.findById(userId);
		if(other == null)
			notFound();
		List<Subscription> s = Subscription.find("SELECT s FROM Subscription s WHERE s.subscriber = ?1 AND s.subscribed = ?2", user, other).fetch();
		for(int x = s.size()-1;x >= 0;x--) {
			s.get(x).delete();
			ok();
		}
		subscriptions(other.id);
	}
	public static void subscriptions(Long id) {
		User user = id == null ? user() : (User) User.findById(id);
		render(user);
	}
}
