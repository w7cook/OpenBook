package controllers;

import java.util.Calendar;

import controllers.Secure;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;;

@With(Secure.class)
public class Events extends Controller{
	
	@Before
	  static void setConnectedUser() {
	    if (Security.isConnected()) {
	      renderArgs.put("currentUser", user());
	    }
	  }

	  public static User user() {
	    assert Secure.Security.connected() != null;
	    return User.find("byEmail", Secure.Security.connected()).first();
	  }
	  
		public static void newEventInvite(Long eventId, User curGuest) {
			Event event = Event.findById(eventId);
			EventInvite myEventInvite = new EventInvite(event, curGuest).save();
			//this.allInvitedUsers.add(curGuest);
			event.save();
			
		}
		
		public static void addEventEndDate(Long eventId, Calendar endDate) {
			Event event = Event.findById(eventId);
			event.endDate = endDate;
			event.save();
			
		}
		
		public static void addEventPrivacy(Long eventId, boolean open, boolean friends, boolean invite){
			Event event = Event.findById(eventId);
			event.open = open;
			event.friends = friends;
			event.invite = invite;
			event.save();
		}
		
		  public static void events(Long id) {
				User user = id == null ? user() : (User) User.findById(id);
				render(user);
		  }
			
		  public static void addEvent() {
				render();
		  }

		public static void addEventInvite(Long eventId, Long guestId){
				User guest = User.findById(guestId);
				newEventInvite(eventId, guest);
			}
			
			public static void deleteEvent(Long eventId, Long page) {
				Event event = Event.findById(eventId);
				event.delete();
				//meow?
				render("events.html");
			}
			
			
			public static void editEvent() {
				//Event event = Event.findById(id);
				render();
			}

			public static void event_create(Event curEvent) {
				User currentUser = user();

				validation.required(curEvent.eventName).message("Event name is required");
				validation.required(curEvent.eventScript).message("Event description is required");
				validation.required(curEvent.eventLocation).message("Event location is required");
				//validation.required(curEvent.startDate).message("Event start date and time are required");		
				//validation.required(curEvent.open || curEvent.friends || curEvent.invite).message("Event privacy is required");

				if (validation.hasErrors()) {
					Event thisEvent = curEvent;
					renderTemplate("Application/addEvent.html", thisEvent);
				} else {
					Event event = curEvent;
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
//					if (given(curEvent.open)) {
//						event.open = TRUE;
//					}
//					if (given(curEvent.friends)) {
//						event.friends = TRUE;
//					}
//					if (given(curEvent.invite)) {
//						event.invite = TRUE;
//					}

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
