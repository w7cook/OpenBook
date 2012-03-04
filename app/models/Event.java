package models;

import java.util.*;
import javax.persistence.*;

import controllers.Application;

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
	public String StartMonth;
	public String eventName;
	//@Lob ??
	public String eventScript;
	public Calendar startDate;
	public Calendar endDate;
	
	public String eventLocation;
	
	
	//future additions
	public boolean open;
	public boolean friends;
	public boolean invite;
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
	
	public Event(){
		
	}
	
	public Event(User author, String eventName, String eventScript, String eventLocation, Calendar startDate){
		//add arguments of (boolean secret, Location eventLocation)
	
		//this.allInvitedUsers = new ArrayList<CUser>();
		//this.attendingUsers = new ArrayList<CUser>();
		//this.maybeAttendingUsers = new ArrayList<CUser>();
		//this.notAttendingUsers = new ArrayList<CUser>();
		
		this.author = author;
		this.eventName = eventName;
		this.eventScript = eventScript;
		this.startDate = startDate;
		this.endDate = endDate;
		this.eventLocation = eventLocation;
		this.open = open;
		this.friends = friends;
		this.invite = invite;
		
		//this.open = TRUE;
		//this.secret = secret;
		//this.eventVenue = eventVenue;
	}
	
	
	public Event newEventInvite(User curGuest) {
		EventInvite myEventInvite = new EventInvite(this, curGuest).save();
		//this.allInvitedUsers.add(curGuest);
		this.save();
		return this;
	}
	
	public Event addEventEndDate(Calendar endDate) {
		this.endDate = endDate;
		this.save();
		return this;
	}
	
}