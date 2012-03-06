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

	public static void displayEvent(Long id) {
		Event e = Event.findById(id);
		String name = e.eventName;
		String location = e.eventLocation;
		Date start = e.startDate;
		Date end = e.endDate;
		render(name, location, start, end);

	}

	public static void event_create(Event curEvent) {
		User currentUser = user();

		validation.required(curEvent.eventName).message(
				"Event name is required");
		validation.required(curEvent.eventScript).message(
				"Event description is required");
		validation.required(curEvent.eventLocation).message(
				"Event location is required");
		validation.isTrue(!curEvent.startMonth.equals("-1")).message(
				"Event start month is required");
		validation.isTrue(!curEvent.startDay.equals("-1")).message(
				"Event start day is required");
		validation.isTrue(!curEvent.startTime.equals("-1")).message(
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
			event.startMonth = curEvent.startMonth;
			event.startDay = curEvent.startDay;
			event.startTime = curEvent.startTime;
			event.setStartDate();

			// optional: set end date of Event
			if (!curEvent.endDay.equals("-1")
					&& !curEvent.endMonth.equals("-1")
					&& !curEvent.endTime.equals("-1")) {
				event.endDay = curEvent.endDay;
				event.endMonth = curEvent.endMonth;
				event.endTime = curEvent.endTime;
				event.setEndDate();
			}

			// privacy settings of Event
			if (curEvent.privelage.equals("open")) {
				event.open = true;
			} else if (curEvent.privelage.equals("friends")) {
				event.friends = true;
			} else if (curEvent.privelage.equals("inviteOnly")) {
				event.inviteOnly = true;
			}

			event.save();
			displayEvent(event.id);

		}
	}

}