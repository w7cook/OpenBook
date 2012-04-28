package controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.Event;
import models.Post;
import models.User;

public class Events extends OBController {

  public static void event(Long eventId) {
    Event event = Event.findById(eventId);
    if(event == null)
      notFound("That event does not exist.");
    String dateString = convertDateToString(event.startDate);
    User user = user();

    String privacy = "";
    if(event.open){ privacy = "Public Event"; }
    else if(event.friends){ privacy = "Friends Event"; }
    else { privacy = "Invite Only Event"; }

    if (event.givenEndDate){
      // Check to see if the month and day are the same.
      DateFormat df = new SimpleDateFormat("MMdd");
      dateString += " until ";
      if (df.format(event.startDate).equals(df.format(event.endDate)))
        dateString += convertDateTime(event.endDate);
      else
        dateString += convertDateToString(event.endDate);
    }
    render(event, user, privacy, dateString);
  }

  public static void events(Long userId) {
    User user = userId == null ? null : (User) User.findById(userId);
    User currentUser = user();
    List<Event> events;
    if(user == null)
      events = Event.findAll();  //TODO: change to public and friends after visibility gets sorted
    else
      events = user.myEvents();
    render(user, currentUser, events);
  }

  public static void upcoming(Long userId) {
    User user = userId == null ? user() : (User) User.findById(userId);
    List<Event> events;
    if(userId == null)
      events = Event.find("SELECT r FROM Event r where r.endDate >= ?", new Date()).fetch();
    else
      events = user.upcomingEvents();
    render(user, events);
  }

  public static void past(Long userId) {
    User user = userId == null ? user() : (User) User.findById(userId);
    List<Event> events;
    if(userId == null)
      events = Event.find("SELECT r FROM Event r where r.endDate < ?", new Date()).fetch();
    else
      events = user.pastEvents();
    render(user, events);
  }



  public static void addEvent() {
    render();
  }

  public static void addEventInvite(Long eventId, Long guestId) {
    User guest = User.findById(guestId);
    Event event = Event.findById(eventId);
    event.addMember(guest);
    event.save();
    event(event.id);
  }

  public static void deleteEvent(Long eventId) {
    Event event = Event.findById(eventId);
    if(event == null)
      notFound("Event does not exist");
    User user = user();
    if(!event.owner.equals(user))
      forbidden();
    event.delete();
    events(user.id);
  }

  public static void event_create(Event curEvent, String startMonth, String startDay, String startTime, String endMonth, String endDay, String endTime) {
    if (params.get("submit") != null) {
      User currentUser = user();
      validation.required(curEvent.name).message("Event name is required");
      validation.required(curEvent.script).message(
          "Event description is required");
      validation.required(curEvent.location).message(
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
        event.owner = currentUser;

        event.name = curEvent.name;
        event.script = curEvent.script;
        event.location = curEvent.location;
        event.startDate = setDate(startTime, startMonth, startDay);

        if (!endMonth.equals("-1") && !endDay.equals("-1")
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
        event.members = new ArrayList<User>();
        event.members.add(currentUser);
        event.save();
        event(event.id);
      }
    }
    else if(params.get("cancel") != null){
      events(user().id);
    }
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
    new Post((Event)Event.findById(eventId), (User)User.findById(userId), post_content).save();
    event(eventId);
  }

  public static void leaveEvent(Long eventId, Long userId){
    User guest = User.findById(userId);
    Event event = Event.findById(eventId);
    event.removeMember(guest);
    event.save();
    events(userId);
  }
}
