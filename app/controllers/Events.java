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
				event.givenEndDate = true;
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
		Date myDateStart = e.startDate;		
		String myDate = convertDateMonth(myDateStart) + " " + myDateStart.getDate() + " at " + convertDateTime(myDateStart);
		String description = e.eventScript;
		String myAuthor = e.author.name;
		
		String privacy = "";
		if(e.open){ privacy = "Public Event"; }
		else if(e.friends){ privacy = "Friends Event"; }
		else { privacy = "Invite Only Event"; }
		
		if (e.givenEndDate){
			Date myDateEnd = e.endDate;			
			if ((myDateStart.getMonth() == myDateEnd.getMonth()) && (myDateStart.getDate() == myDateEnd.getDate())){
				myDate += " until " + convertDateTime(myDateEnd);
			}
			else {
				myDate += " until " + convertDateMonth(myDateEnd) + " " + myDateEnd.getDate() + " at " + convertDateTime(myDateEnd);
			}
		}
		render(name, privacy, myAuthor, myDate, location, description);
	}
	
	public static String convertDateMonth(Date myDate){
		int month = myDate.getMonth();
		if (month == 0){ return "January"; }
		else if (month == 1){ return "February"; }
		else if (month == 2){ return "March"; }
		else if (month == 3){ return "April"; }
		else if (month == 4){ return "May"; }
		else if (month == 5){ return "June"; }
		else if (month == 6){ return "July"; }
		else if (month == 7){ return "August"; }
		else if (month == 8){ return "September"; }
		else if (month == 9){ return "October"; }
		else if (month == 10){ return "November"; }
		else { return "December"; }
	}
	
	public static String convertDateTime(Date myDate){
		String ret = "";
		int hour = myDate.getHours(); 
		String ampm = "";
		if (hour == 0) { hour = 12; ampm = "AM"; }
		else if (hour >= 1 && hour <= 11) { ampm = "AM"; }
		else{ hour = hour - 12; ampm = "PM"; }
		ret += hour;
		int minutes= myDate.getMinutes();
		if (minutes < 9){ ret += ":0" + minutes; }
		else { ret += ":" + minutes; }
		ret += " " + ampm;
		return ret;
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
