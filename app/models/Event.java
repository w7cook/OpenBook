package models;

import java.util.*;
import javax.persistence.*;
//necessary?
import play.data.validation.*;

import controllers.Events;

import play.db.jpa.*;

@Entity
public class Event extends Model {

	@ManyToOne
	public User author;
	
	/*
	//invitations list
	@OneToMany
	public EventInvite invitedUsers;
	//possible friends to invite handled in EventInvite
	*/
	public String startMonth;
	public String startDay;
	public String startTime;
	
	public String endMonth;
	public String endDay;
	public String endTime;
	
	public String eventName;
	public String eventScript;
	public Date startDate;
	public Date endDate;
	
	public String eventLocation;
	
	
	//future additions
	public String p;
	public boolean open = false;
	public boolean friends = false;
	public boolean inviteOnly = false;
	//public Location eventVenue;
	
	
	/*
	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
	public List<User> allInvitedUsers;
	
	public List<User> attendingUsers() {
		return User.find("event = ? AND isAttending=TRUE", this).fetch();
	}
	public List<User> maybeAttendingUsers() {
		return User.find("event = ? AND maybeAttending=TRUE", this).fetch();
	}
	public List<User> notAttendingUsers() {
		return User.find("event = ? AND declinedAttending=TRUE", this).fetch();
	}
	
	//should all of this be taking a list of EventInvite
	//or possibly add on .getGuest() before .find()...
	*/
	
	public Event(User author, String eventName, String eventScript, String eventLocation){//, Calendar startDate){
	
		//this.allInvitedUsers = new ArrayList<CUser>();
		//this.attendingUsers = new ArrayList<CUser>();
		//this.maybeAttendingUsers = new ArrayList<CUser>();
		//this.notAttendingUsers = new ArrayList<CUser>();
		
		this.author = author;
		this.eventName = eventName;
		this.eventScript = eventScript;
		this.eventLocation = eventLocation;


	}
	

	public EventInvite newEventInvite(User curGuest) {
		//Event event = Event.findById(eventId);
		EventInvite myEventInvite = new EventInvite(this, curGuest).save();
		//this.allInvitedUsers.add(curGuest);
		this.save();
		return myEventInvite;
	}
	
	public void setStartDate(){
		int minutes = 0;
		int hours = 0;
		if(startTime.length() == 4){
			minutes = Integer.parseInt(startTime.substring(2,4));
			hours = Integer.parseInt(startTime.substring(0,2));
		}
		else{
			hours = Integer.parseInt(startTime);
		}
		startDate = new Date(Calendar.getInstance().get(Calendar.YEAR) - 1900, Integer.parseInt(startMonth), Integer.parseInt(startDay), hours, minutes);	
	}
	
	public void setEndDate(){
		int minutes = 0;
		int hours = 0;
		if(endTime.length() == 4){
			minutes = Integer.parseInt(endTime.substring(2,4));
			hours = Integer.parseInt(endTime.substring(0,2));
		}
		else{
			hours = Integer.parseInt(endTime);
		}
		endDate = new Date(Calendar.getInstance().get(Calendar.YEAR) - 1900, Integer.parseInt(endMonth), Integer.parseInt(endDay), hours, minutes);	
	}
	
}