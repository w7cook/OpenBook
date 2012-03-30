package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Subscriptions extends OBController {

	public static void addSubscription(Long id) {
		
	}
	
	public static void subscriptions(Long id) {
		User user = id == null ? user() : (User) User.findById(id);
		render(user);
	}
}
