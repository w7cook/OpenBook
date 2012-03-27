package controllers;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import models.Event;
import models.Post;
import models.User;
import play.mvc.Before;
import play.mvc.With;

@With(Secure.class)
public class Events extends OBController {

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
			event.members= new ArrayList<User>();
			event.members.add(currentUser);
			event.save();
			displayEvent(event.id);

		}
	}
	
	public static void displayEvent(Long id) {
		Event e = Event.findById(id);
		String name = e.eventName;
		String location = e.eventLocation;
		Date myDateStart = e.startDate;		
		String myDate = convertDateToString(myDateStart);
		String description = e.eventScript;
		User user = user();
		String myAuthor = e.author.name;
		
		String privacy = "";
		if(e.open){ privacy = "Public Event"; }
		else if(e.friends){ privacy = "Friends Event"; }
		else { privacy = "Invite Only Event"; }
		
		if (e.givenEndDate){
			Date myDateEnd = e.endDate;
			// Check to see if the month and day are the same.
			DateFormat df = new SimpleDateFormat("MMdd");
			if (df.format(myDateStart).equals(df.format(myDateEnd))){
				myDate += " until " + convertDateTime(myDateEnd);
			}
			else {
				myDate += " until " + convertDateToString(myDateEnd);
			}
		}
		render(e, user, name, privacy, myAuthor, myDate, location, description);
	}
	
	public static String convertDateToString(Date myDate){
	  DateFormat df = new SimpleDateFormat("MMMM d 'at' HH:mm a");
    return df.format(myDate);
	}
	
	public static String convertDateTime(Date myDate){
	  DateFormat df = new SimpleDateFormat("HH:mm a");
		return df.format(myDate);
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
		// Using Calendar because date manipulation with Date is deprecated.
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), Integer.parseInt(month), Integer.parseInt(day), hours, minutes);
		return cal.getTime();
	}
	
	public static void newEventPost(Long eventId, Long userId, String post_content){
		new Post((User)User.findById(userId), eventId.toString(), post_content, Post.type.EVENT).save();
		events(eventId);
	}
}
