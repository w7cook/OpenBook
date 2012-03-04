package controllers;

import java.util.*;

import controllers.Secure;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;;

//<td colspan=3><a class=button  href="@{Events.editEvent()}">Create </a>


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
	  
	  private static boolean given(String val) {
		    return val != null && val.length() > 0;
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
				Event event = Event.findById(eventId);
				event.newEventInvite(guest);
			}
		
		public static void deleteEvent(Long eventId, Long userId) {
			Event event = Event.findById(eventId);
			event.delete();
			events(userId);
		}
			
			public static void displayEvent(Long id){//displayEvent(String name, String location, Date startDate, Date  endDate) {
				Event e = Event.findById(id);
				String name = e.eventName;
				String location = e.eventLocation;
				Date start = e.startDate;
				Date end = e.endDate;
				render(name, location, start, end);
			}

			public static void event_create(Event curEvent) {
				User currentUser = user();

				validation.required(curEvent.eventName).message("Event name is required");
				validation.required(curEvent.eventScript).message("Event description is required");
				validation.required(curEvent.eventLocation).message("Event location is required");
				validation.isTrue(!curEvent.startMonth.equals("-1")).message("Event start month is required");	
				validation.isTrue(!curEvent.startDay.equals("-1")).message("Event start day is required");	
				validation.isTrue(!curEvent.startTime.equals("-1")).message("Event start time is required");
				//validation.required(curEvent.startDate).message("Event start date and time are required");		
				//validation.required(curEvent.open || curEvent.friends || curEvent.invite).message("Event privacy is required");

				if (validation.hasErrors()) {
					Event thisEvent = curEvent;
					renderTemplate("Events/addEvent.html", thisEvent);
				} else {
					Event event = curEvent;
					event.author = currentUser;

					event.eventName = curEvent.eventName;
					event.eventScript = curEvent.eventScript;
					event.eventLocation = curEvent.eventLocation;
					event.startMonth = curEvent.startMonth;
					event.startDay = curEvent.startDay;
					event.startTime = curEvent.startTime;
					event.setStartDate();
					

					//event.startDate = curEvent.startDate;
				
					if (!curEvent.endDay.equals("-1") && !curEvent.endMonth.equals("-1") && !curEvent.endTime.equals("-1")) {
						event.endDay = curEvent.endDay;
						event.endMonth = curEvent.endMonth;
						event.endTime = curEvent.endTime;
						event.setEndDate();
					}
					
					
					if (curEvent.p.equals("open")){
						event.open = true;
					}
					else if (curEvent.p.equals("friends")){
						event.friends = true;
					}
					else if (curEvent.p.equals("inviteOnly")){
						event.inviteOnly = true;
					}
					
//					System.out.println(event.startDate);
//					System.out.println(event.endDate);
//					System.out.println(event.open + " " + event.friends + " " + event.inviteOnly);
					
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
					displayEvent(event.id);
					//displayEvent(event.eventName, event.eventLocation, event.startDate, event.endDate);
				}
			}

}
