package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import models.*;

public class Relationships extends OBController {



	/**
	 * Request to be friends with a given user, changing appropriate
	 * Relationship flags where necessary.
	 * 
	 * @param id
	 *            the user to request friendship with
	 */
	public static void requestFriends(Long id) {
		User user = user();
		User other = User.findById(id);
		Relationship r1 = Relationship.find(
				"SELECT r FROM Relationship r where r.from = ? AND r.to = ?",
				user, other).first();
		Relationship r2 = Relationship.find(
				"SELECT r FROM Relationship r where r.to = ? AND r.from = ?",
				user, other).first();

		if (r1 == null) {
			r1 = new Relationship(user, other, true);
		}

		if (r2 != null) {

			// If the other user has already requested, this request should make
			// them friends
			if (r2.requested) {
				r1.requested = false;
				r2.requested = false;
				r1.accepted = true;
				r2.accepted = true;
				r1.save();
				r2.save();
			} else if (r2.accepted) {
				Application.news(id);
				return;
			} else {
				r1.requested = true;
			}
		}
		// If the user has already made a request for friendship, do nothing
		else if (r1 != null) {
			if (r1.requested) {
				Application.news(id);
				return;
			} else {
				r1.requested = true;
				r1.save();
				Application.news(id);
				return;
			}
		}
		r1.save();
		Application.news(id);
	}

	/**
	 * Attempt to end a relationship with a user, changing appropriate
	 * Relationship flags where necessary
	 * 
	 * @param id
	 *            the user to remove
	 */
	public static void removeFriends(Long id) {
		User user = user();
		User other = User.findById(id);
		Relationship r1 = Relationship.find(
				"SELECT r FROM Relationship r where r.from = ? AND r.to = ?",
				user, other).first();
		Relationship r2 = Relationship.find(
				"SELECT r FROM Relationship r where r.to = ? AND r.from = ?",
				user, other).first();

		if (r1 != null) {
			r1.requested = false;
			r1.accepted = false;
			r1.save();
		}
		if (r2 != null) {
			r2.accepted = false;
			r2.requested = false;
			r2.save();
		}
		Application.news(id);
	}
	
	/**
	 * hello	
	 */
	
	public static void relationships(Long id) {
		User user = id == null ? user() : (User) User.findById(id);
		render(user);		
	}
	
}
