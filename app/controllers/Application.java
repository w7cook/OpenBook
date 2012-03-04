package controllers;

import java.util.*;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

@With(Secure.class)
public class Application extends Controller {
 
  
  @Before
  static void setConnectedUser(){
    if (Security.isConnected()) {
      renderArgs.put("currentUser", user());
    }
  }
  
  @Before
  static void addDefaults() {
    
  }

  public static User user() {
    assert Secure.Security.connected() != null;
    return User.find("byEmail", Secure.Security.connected()).first();
  }

  public static void about(Long id) {
    User user = id == null ? user() : (User) User.findById(id);
    render(user);
  }

  public static void news(Long id) {
    User user = id == null ? user() : (User) User.findById(id);
    render(user);
  }

  public static void account() {
    User user = user();
    render(user);
  }

  private static boolean given(String val) {
    return val != null && val.length() > 0;
  }

  /** Request to be friends with a given user, changing appropriate Relationship flags where necessary.
   *
   * @param id the user to request friendship with
   */
  public static void requestFriends(Long id) {
    User user = user();
    User other = User.findById(id);
    Relationship r1 = Relationship.find("SELECT r FROM Relationship r where r.from = ? AND r.to = ?", user, other).first();
    Relationship r2 = Relationship.find("SELECT r FROM Relationship r where r.to = ? AND r.from = ?", user, other).first();

    if (r1 == null) {
      r1 = new Relationship(user, other, true);
    }

    if (r2 != null) {

      // If the other user has already requested, this request should make them friends
      if (r2.requested) {
        r1.requested = false;
        r2.requested = false;
        r1.accepted = true;
        r2.accepted = true;
        r1.save();
        r2.save();
      } else if (r2.accepted){
        news(id);
        return;
      } else {
        r1.requested = true;
      }
    }
    // If the user has already made a request for friendship, do nothing
    else if (r1 != null) {
      if (r1.requested) {
        news(id);
        return;
      } else {
        r1.requested = true;
        r1.save();
        news(id);
        return;
      }
    }
    r1.save();
    news(id);
  }

  /** Attempt to end a relationship with a user, changing appropriate Relationship flags where necessary
   *
   * @param id the user to remove
   */
  public static void removeFriends(Long id) {
    User user = user();
    User other = User.findById(id);
    Relationship r1 = Relationship.find("SELECT r FROM Relationship r where r.from = ? AND r.to = ?", user, other).first();
    Relationship r2 = Relationship.find("SELECT r FROM Relationship r where r.to = ? AND r.from = ?", user, other).first();

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
    news(id);
  }

  public static void account_save(User update, String old_password) {
    User currentUser = user();

    validation.required(update.first_name).message("First name is required");
    validation.required(update.username).message("Username is required");
    validation.required(update.email).message("Email is required");
    validation.isTrue(currentUser.password.equals(old_password)).message(
        "Password does not match");

    if (validation.hasErrors()) {
      User user = update;
      renderTemplate("Application/account.html", user);
    } else {
      User user = currentUser;
      String name = "";
      if (given(update.first_name)) {
        user.first_name = update.first_name;
        name += user.first_name;
      }
      if (given(update.middle_name)) {
        user.middle_name = update.middle_name;
        if (given(name))
          name += " ";
        name += user.first_name;
      }
      if (given(update.last_name)) {
        user.last_name = update.last_name;
        if (given(name))
          name += " ";
        name += user.first_name;
      }
      user.name = name;
      user.username = update.username;
      user.email = update.email;
      if (given(update.password))
        user.password = update.password;
      user.save();
      account();
    }
  }

  public static void edit_basic() {
    long userID = 1;
    User user = User.findById(userID);
    render(user);
  }

  public static void updateBasic() {
    long userID = 1;
    User user = User.findById(userID);
    render(user);
  }

  public static void search(String query) {
    // not implemented yet
  }

  public static void deleteComment(Long id, Long userId) {
    Comment c = Comment.findById(id);
    c.delete();
    news(userId);
  }

  public static void postComment(Long commentableId, String author, String content) {
    Commentable parent = Commentable.findById(commentableId);
    parent.addComment(author, content);
  }


  public static void notFound() {
    response.status = Http.StatusCode.NOT_FOUND;
    renderText("");
  }

  public static void events(Long id) {
		User user = id == null ? user() : (User) User.findById(id);
		render(user);
  }
	
  public static void addEvent() {
		render();
  }

public static void addEventInvite(Long eventId, Long guestId){
		Event event = Event.findById(eventId);
		User guest = User.findById(guestId);
		event.newEventInvite(guest);
	}
	
	public static void deleteEvent(Long eventId, Long page) {
		Event event = Event.findById(eventId);
		event.delete();
		news(page);
	}
	
	
	public static void editEvent() {
		//Event event = Event.findById(id);
		render();
	}

	public static void event_create(Event curEvent) {
		User currentUser = user();
		Event newEvent = new Event();

		validation.required(curEvent.eventName).message("Event name is required");
		validation.required(curEvent.eventScript).message("Event description is required");
		validation.required(curEvent.eventLocation).message("Event location is required");
		//validation.required(curEvent.startDate).message("Event start date and time are required");		
		//validation.required(curEvent.open || curEvent.friends || curEvent.invite).message("Event privacy is required");

		if (validation.hasErrors()) {
			Event thisEvent = curEvent;
			renderTemplate("Application/addEvent.html", thisEvent);
		} else {
			Event event = newEvent;
			event.author = currentUser;

			event.eventName = curEvent.eventName;
			event.eventScript = curEvent.eventScript;
			event.eventLocation = curEvent.eventLocation;
			System.out.println(curEvent.eventName);
			/*
			event.startDate = curEvent.startDate;
			if (given(curEvent.endDate)) {
				event.endDate = curEvent.endDate;
			}
			*/
//			if (given(curEvent.open)) {
//				event.open = TRUE;
//			}
//			if (given(curEvent.friends)) {
//				event.friends = TRUE;
//			}
//			if (given(curEvent.invite)) {
//				event.invite = TRUE;
//			}

			event.save();
			//this should pull up to an extended events page
			//add a Locatiom
			//invite guests
			//upload event photos
			//upload newsfeed
			
			editEvent();
		}
	}
}
