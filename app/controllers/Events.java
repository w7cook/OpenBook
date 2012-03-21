package controllers;

import java.util.*;

import controllers.Secure;

import play.*;
import play.mvc.*;
import controllers.Secure;
import models.*;

;

@With(Secure.class)
public class Events extends OBController {

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

	public static void addEventInvite(Long eventId, Long guestId) {
		User guest = User.findById(guestId);
		Event event = Event.findById(eventId);
		event.newEventInvite(guest);
	}

	public static void deleteEvent(Long eventId, Long userId) {
		Event event = Event.findById(eventId);
		event.delete();
		events(userId);
	}

	public static void event_create(Event curEvent, String startMonth, String startDay, String startTime, String endMonth, String endDay, String endTime) {
		User currentUser = user();

		validation.required(curEvent.eventName).message(
				"Event name is required");
		validation.required(curEvent.eventScript).message(
				"Event description is required");
		validation.required(curEvent.eventLocation).message(
				"Event location is required");
		validation.isTrue(!startMonth.equals("-1")).message(
				"Event start month is required");
		validation.isTrue(!startDay.equals("-1")).message(
				"Event start day is required");
		validation.isTrue(!startTime.equals("-1")).message(
				"Event start time is required");

		if (validation.hasErrors()) {
			Event thisEvent = curEvent;
			renderTemplate("Events/addEvent.html", thisEvent);
		} else {
			Event event = curEvent;
			event.author = currentUser;

			event.eventName = curEvent.eventName;
			event.eventScript = curEvent.eventScript;
			event.eventLocation = curEvent.eventLocation;		
			event.startDate = setDate(startTime, startMonth, startDay);

			if (!endMonth.equals("-1") 
				&& !endDay.equals("-1") 
				&& !endTime.equals("-1")) {
				event.endDate = setDate(endTime, endMonth, endDay);
			}

			if (curEvent.privilege.equals("open")) {
				event.open = true;
			} else if (curEvent.privilege.equals("friends")) {
				event.friends = true;
			} else if (curEvent.privilege.equals("inviteOnly")) {
				event.inviteOnly = true;
			}

			event.save();
			displayEvent(event.id);

		}
	}
	
	public static void displayEvent(Long id) {
		Event e = Event.findById(id);
		String name = e.eventName;
		String location = e.eventLocation;
		Date start = e.startDate;
		Date end = e.endDate;
		render(name, location, start, end);
	}
	
	public static Date setDate(String time, String month, String day){
		int minutes = 0;
		int hours = 0;
		if(time.length() == 4){
			minutes = Integer.parseInt(time.substring(2,4));
			hours = Integer.parseInt(time.substring(0,2));
		}
		else{
			hours = Integer.parseInt(time);
		}
		Date newDate = new Date(Calendar.getInstance().get(Calendar.YEAR) - 1900, Integer.parseInt(month), Integer.parseInt(day), hours, minutes);	
		return newDate;
	}
}
